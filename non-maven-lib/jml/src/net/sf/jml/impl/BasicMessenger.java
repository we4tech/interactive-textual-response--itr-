/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jml.impl;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

import net.sf.cindy.Session;
import net.sf.cindy.SessionAdapter;
import net.sf.cindy.SessionListener;
import net.sf.cindy.impl.SocketSession;
import net.sf.jml.Email;
import net.sf.jml.MsnClientId;
import net.sf.jml.MsnConnection;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnContactList;
import net.sf.jml.MsnMessageChain;
import net.sf.jml.MsnMessageIterator;
import net.sf.jml.MsnOwner;
import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.event.MsnSwitchboardAdapter;
import net.sf.jml.protocol.MsnMessage;
import net.sf.jml.protocol.MsnOutgoingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.incoming.IncomingXFR;
import net.sf.jml.protocol.outgoing.OutgoingCVR;
import net.sf.jml.protocol.outgoing.OutgoingPNG;
import net.sf.jml.protocol.outgoing.OutgoingUSRInitNS;
import net.sf.jml.protocol.outgoing.OutgoingVER;

/**
 * basic MsnMessenger implement, communication with MSN DS/NS
 * 
 * @author Roger Chen
 */
public abstract class BasicMessenger extends AbstractMessenger {

    private final MsnOwnerImpl owner;
    private final MsnContactListImpl contactList;

    /**
     * Connection information
     */
    private MsnConnectionImpl connection;

    /**
     * MsnSession communication with DS/NS server
     */
    private MsnSession session;

    /**
     * Listeners for pre-login events.
     */
    private Vector preLoginSessionListeners = new Vector();

    /**
     * Switchboards, communication with SB server
     */
    private final Set switchboards = new HashSet();

    public BasicMessenger(Email email, String password) {
        owner = new MsnOwnerImpl(this, email, password);
        contactList = new MsnContactListImpl(this);
    }

    public MsnOwner getOwner() {
        return owner;
    }

    public MsnContactList getContactList() {
        return contactList;
    }

    public MsnConnection getConnection() {
        return connection;
    }

    public void login() {
        login("messenger.hotmail.com", 1863);
    }

    public synchronized void logout() {
        if (session == null)
            return;
        MsnSwitchboard[] sbs = getActiveSwitchboards();
        for (int i = 0; i < sbs.length; i++) {
            sbs[i].close();
        }
        synchronized (switchboards) {
            switchboards.clear();
        }
        session.close();
    }

    public boolean send(MsnOutgoingMessage message, boolean block) {
        if (session != null) {
            if (block)
                return session.sendSynchronousMessage(message);
            else
                session.sendAsynchronousMessage(message);
        }
        return false;
    }

    public MsnSwitchboard[] getActiveSwitchboards() {
        synchronized (switchboards) {
            MsnSwitchboard[] sbs = new MsnSwitchboard[switchboards.size()];
            switchboards.toArray(sbs);
            return sbs;
        }
    }

    public MsnMessageChain getIncomingMessageChain() {
        if (session == null)
            return null;
        return session.getIncomingMessageChain();
    }

    public MsnMessageChain getOutgoingMessageChain() {
        if (session == null)
            return null;
        return session.getOutgoingMessageChain();
    }

    public synchronized void login(String ip, int port) {
        if (session != null && session.isAvailable())
            return;

        connection = new MsnConnectionImpl();
        connection.setRemoteIP(ip);
        connection.setRemotePort(port);

        session = new MsnSession(this, new InetSocketAddress(ip, port));
        session.addSessionListener(new MessengerSessionListener());
        for (int i = 0, len = preLoginSessionListeners.size(); i < len; i++) {
            session.addSessionListener((SessionListener)preLoginSessionListeners.get(i));
            
        }

        session.start();
    }

    public void setActualMsnProtocol(MsnProtocol protocol) {
        super.setActualMsnProtocol(protocol);
        owner.fSetClientId(MsnClientId.getDefaultSupportedClientId(protocol));
    }

    public void addSessionListener(SessionListener listener) {
        if (session != null) {
            session.addSessionListener(listener);
        }
        else {
            preLoginSessionListeners.add(listener);
        }
    }

    public String toString() {
        return "MsnMessenger: " + owner.getEmail();
    }

    public void sendText(final Email email, final String text) {
        if (email == null || text == null)
            return;
        MsnSwitchboard[] switchboards = getActiveSwitchboards();
        for (int i = 0; i < switchboards.length; i++) {
            if (switchboards[i].containContact(email)
                    && switchboards[i].getAllContacts().length == 1) {
                switchboards[i].sendText(text);
                return;
            }
        }

        final Object attachment = new Object();
        addSwitchboardListener(new MsnSwitchboardAdapter() {

            public void switchboardStarted(MsnSwitchboard switchboard) {
                if (switchboard.getAttachment() == attachment) {
                    switchboard.inviteContact(email);
                }
            }

            public void contactJoinSwitchboard(MsnSwitchboard switchboard,
                    MsnContact contact) {
                if (switchboard.getAttachment() == attachment
                        && email.equals(contact.getEmail())) {
                    switchboard.setAttachment(null);
                    removeSwitchboardListener(this);
                    switchboard.sendText(text);
                }
            }

        });
        newSwitchboard(attachment);
    }

    /**
     * Start a new switchboard use the given infomation.
     * 
     * @param ip
     * 		connect ip
     * @param port
     * 		connect port
     * @param createdByOwner
     * 		is this switchboard created by owner
     * @param authStr
     * 		switchboard authStr
     * @param sessionId
     * 		switchboard session id
     * @param attachment
     * 		attachment
     * @return
     * 		switchboard
     */
    public MsnSwitchboard newSwitchboard(String ip, int port,
            boolean createdByOwner, String authStr, int sessionId,
            Object attachment) {
        final SimpleSwitchboard switchboard = new SimpleSwitchboard(this,
                createdByOwner, ip, port);
        switchboard.setAuthStr(authStr);
        switchboard.addSessionListener(new SessionAdapter() {

            public void sessionEstablished(Session session) {
                synchronized (switchboards) {
                    switchboards.add(switchboard);
                }
            }

            public void sessionClosed(Session session) {
                synchronized (switchboards) {
                    switchboards.remove(switchboard);
                }
            }
        });
        switchboard.setSessionId(sessionId);
        switchboard.setAttachment(attachment);
        switchboard.start();
        return switchboard;
    }

    /**
     * MsnMessenger session listener.
     * 
     * @author Roger Chen
     */
    private class MessengerSessionListener extends SessionAdapter {

        public void sessionEstablished(Session session) {
            Socket socket = ((SocketSession) session).getChannel().socket();
            connection.setInternalIP(socket.getLocalAddress().getHostAddress());
            connection.setInternalPort(socket.getLocalPort());

            OutgoingVER ver = new OutgoingVER(null);
            ver.setSupportedProtocol(getSupportedProtocol());
            send(ver, false);

            OutgoingCVR cvr = new OutgoingCVR(null);
            cvr.setEmail(owner.getEmail());
            send(cvr, false);

            OutgoingUSRInitNS usr = new OutgoingUSRInitNS(null);
            usr.setEmail(owner.getEmail());
            send(usr, false);
        }

        public void sessionTimeout(Session session) {
            OutgoingPNG message = new OutgoingPNG(getActualMsnProtocol());
            send(message, false);
        }

        public void sessionClosed(Session session) {
            MsnMessageChain chain = ((MsnSession) session.getAttachment())
                    .getIncomingMessageChain();
            MsnMessageIterator iterator = chain.iterator();
            if (iterator.hasPrevious()) {
                MsnMessage message = iterator.previous();
                //The last message is redirect message
                if (message instanceof IncomingXFR
                        && !((IncomingXFR) message)
                                .isTransferredToSwitchboard())
                    return;
            }
            logout();
            fireLogout();

            connection = null;
        }

    }

}
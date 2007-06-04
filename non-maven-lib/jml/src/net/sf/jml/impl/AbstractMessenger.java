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

import java.util.Collection;
import java.util.Iterator;

import net.sf.cindy.util.CopyOnWriteCollection;
import net.sf.jml.*;
import net.sf.jml.event.MsnAdapter;
import net.sf.jml.event.MsnContactListListener;
import net.sf.jml.event.MsnFileTransferListener;
import net.sf.jml.event.MsnMessageListener;
import net.sf.jml.event.MsnMessengerListener;
import net.sf.jml.event.MsnSwitchboardListener;
import net.sf.jml.message.MsnDatacastMessage;
import net.sf.jml.message.MsnInstantMessage;
import net.sf.jml.message.MsnSystemMessage;
import net.sf.jml.message.MsnControlMessage;
import net.sf.jml.message.MsnUnknownMessage;
import net.sf.jml.protocol.MsnOutgoingMessage;

/**
 * Implement MsnMessenger basic method.
 * 
 * @author Roger Chen
 */
public abstract class AbstractMessenger implements MsnMessenger {

    private Object attachment;
    private boolean logIncoming;
    private boolean logOutgoing;

    private MsnProtocol[] supportedProtocol = MsnProtocol
            .getAllSupportedProtocol(); //The Protocol that MsnMessenger supported
    private MsnProtocol actualMsnProtocol; //The protocol that used 

    private final Collection messengerListeners = new CopyOnWriteCollection();
    private final Collection messageListeners = new CopyOnWriteCollection();
    private final Collection contactListListeners = new CopyOnWriteCollection();
    private final Collection switchboardListeners = new CopyOnWriteCollection();
    private final Collection fileTransferListeners = new CopyOnWriteCollection();

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    public boolean isLogIncoming() {
        return logIncoming;
    }

    public void setLogIncoming(boolean logIncoming) {
        this.logIncoming = logIncoming;
    }

    public boolean isLogOutgoing() {
        return logOutgoing;
    }

    public void setLogOutgoing(boolean logOutgoing) {
        this.logOutgoing = logOutgoing;
    }

    public MsnProtocol[] getSupportedProtocol() {
        return supportedProtocol;
    }

    public void setSupportedProtocol(MsnProtocol[] supportedProtocol) {
        if (supportedProtocol != null && supportedProtocol.length > 0)
            this.supportedProtocol = supportedProtocol;
    }

    public MsnProtocol getActualMsnProtocol() {
        return actualMsnProtocol;
    }

    public void setActualMsnProtocol(MsnProtocol protocol) {
        this.actualMsnProtocol = protocol;
    }

    public void send(MsnOutgoingMessage message) {
        send(message, false);
    }

    //  Listener

    public void addListener(MsnAdapter listener) {
        addMessengerListener(listener);
        addMessageListener(listener);
        addContactListListener(listener);
        addSwitchboardListener(listener);
        addFileTransferListener(listener);
    }

    //MessengerListener

    public void addMessengerListener(MsnMessengerListener listener) {
        if (listener != null) {
            messengerListeners.add(listener);
        }
    }

    public void removeMessengerListener(MsnMessengerListener listener) {
        if (listener != null) {
            messengerListeners.remove(listener);
        }
    }

    public void fireLoginCompleted() {
        for (Iterator iter = messengerListeners.iterator(); iter.hasNext();) {
            MsnMessengerListener listener = (MsnMessengerListener) iter.next();
            listener.loginCompleted(this);
        }
    }

    public void fireLogout() {
        for (Iterator iter = messengerListeners.iterator(); iter.hasNext();) {
            MsnMessengerListener listener = (MsnMessengerListener) iter.next();
            listener.logout(this);
        }
    }

    public void fireExceptionCaught(Throwable throwable) {
        for (Iterator iter = messengerListeners.iterator(); iter.hasNext();) {
            MsnMessengerListener listener = (MsnMessengerListener) iter.next();
            listener.exceptionCaught(this, throwable);
        }
    }

    //MessageListener

    public void addMessageListener(MsnMessageListener listener) {
        if (listener != null) {
            messageListeners.add(listener);
        }
    }

    public void removeMessageListener(MsnMessageListener listener) {
        if (listener != null) {
            messageListeners.remove(listener);
        }
    }

    public void fireInstantMessageReceived(MsnSwitchboard switchboard,
            MsnInstantMessage message, MsnContact contact) {
        for (Iterator iter = messageListeners.iterator(); iter.hasNext();) {
            MsnMessageListener listener = (MsnMessageListener) iter.next();
            listener.instantMessageReceived(switchboard, message, contact);
        }
    }

    public void fireControlMessageReceived(MsnSwitchboard switchboard,
            MsnControlMessage message, MsnContact contact) {
        for (Iterator iter = messageListeners.iterator(); iter.hasNext();) {
            MsnMessageListener listener = (MsnMessageListener) iter.next();
            listener.controlMessageReceived(switchboard, message, contact);
        }
    }

    public void fireSystemMessageReceived(MsnSystemMessage message) {
        for (Iterator iter = messageListeners.iterator(); iter.hasNext();) {
            MsnMessageListener listener = (MsnMessageListener) iter.next();
            listener.systemMessageReceived(this, message);
        }
    }

    public void fireDatacastMessageReceived(MsnSwitchboard switchboard,
            MsnDatacastMessage message, MsnContact contact) {
        for (Iterator iter = messageListeners.iterator(); iter.hasNext();) {
            MsnMessageListener listener = (MsnMessageListener) iter.next();
            listener.datacastMessageReceived(switchboard, message, contact);
        }
    }

    public void fireUnknownMessageReceived(MsnSwitchboard switchboard,
            MsnUnknownMessage message, MsnContact contact) {
        for (Iterator iter = messageListeners.iterator(); iter.hasNext();) {
            MsnMessageListener listener = (MsnMessageListener) iter.next();
            listener.unknownMessageReceived(switchboard, message, contact);
        }
    }

    //ContactListListener

    public void addContactListListener(MsnContactListListener listener) {
        if (listener != null) {
            contactListListeners.add(listener);
        }
    }

    public void removeContactListListener(MsnContactListListener listener) {
        if (listener != null) {
            contactListListeners.remove(listener);
        }
    }

    public void fireContactListSyncCompleted() {
        for (Iterator iter = contactListListeners.iterator(); iter.hasNext();) {
            MsnContactListListener listener = (MsnContactListListener) iter
                    .next();
            listener.contactListSyncCompleted(this);
        }
    }

    public void fireContactListInitCompleted() {
        for (Iterator iter = contactListListeners.iterator(); iter.hasNext();) {
            MsnContactListListener listener = (MsnContactListListener) iter
                    .next();
            listener.contactListInitCompleted(this);
        }
    }

    public void fireContactStatusChanged(MsnContact contact) {
        for (Iterator iter = contactListListeners.iterator(); iter.hasNext();) {
            MsnContactListListener listener = (MsnContactListListener) iter
                    .next();
            listener.contactStatusChanged(this, contact);
        }
    }

    public void fireOwnerStatusChanged() {
        for (Iterator iter = contactListListeners.iterator(); iter.hasNext();) {
            MsnContactListListener listener = (MsnContactListListener) iter
                    .next();
            listener.ownerStatusChanged(this);
        }
    }

    public void fireContactAddedMe(MsnContact contact) {
        for (Iterator iter = contactListListeners.iterator(); iter.hasNext();) {
            MsnContactListListener listener = (MsnContactListListener) iter
                    .next();
            listener.contactAddedMe(this, contact);
        }
    }

    public void fireContactRemovedMe(MsnContact contact) {
        for (Iterator iter = contactListListeners.iterator(); iter.hasNext();) {
            MsnContactListListener listener = (MsnContactListListener) iter
                    .next();
            listener.contactRemovedMe(this, contact);
        }
    }

    public void fireContactAddCompleted(MsnContact contact) {
        for (Iterator iter = contactListListeners.iterator(); iter.hasNext();) {
            MsnContactListListener listener = (MsnContactListListener) iter
                    .next();
            listener.contactAddCompleted(this, contact);
        }
    }

    public void fireContactRemoveCompleted(MsnContact contact) {
        for (Iterator iter = contactListListeners.iterator(); iter.hasNext();) {
            MsnContactListListener listener = (MsnContactListListener) iter
                    .next();
            listener.contactRemoveCompleted(this, contact);
        }
    }

    public void fireGroupAddCompleted(MsnGroup group) {
        for (Iterator iter = contactListListeners.iterator(); iter.hasNext();) {
            MsnContactListListener listener = (MsnContactListListener) iter
                    .next();
            listener.groupAddCompleted(this, group);
        }
    }

    public void fireGroupRemoveCompleted(MsnGroup group) {
        for (Iterator iter = contactListListeners.iterator(); iter.hasNext();) {
            MsnContactListListener listener = (MsnContactListListener) iter
                    .next();
            listener.groupRemoveCompleted(this, group);
        }
    }

    //SwitchboardListener

    public void addSwitchboardListener(MsnSwitchboardListener listener) {
        if (listener != null) {
            switchboardListeners.add(listener);
        }
    }

    public void removeSwitchboardListener(MsnSwitchboardListener listener) {
        if (listener != null) {
            switchboardListeners.remove(listener);
        }
    }

    public void fireSwitchboardStarted(MsnSwitchboard switchboard) {
        for (Iterator iter = switchboardListeners.iterator(); iter.hasNext();) {
            MsnSwitchboardListener listener = (MsnSwitchboardListener) iter
                    .next();
            listener.switchboardStarted(switchboard);
        }
    }

    public void fireSwitchboardClosed(MsnSwitchboard switchboard) {
        for (Iterator iter = switchboardListeners.iterator(); iter.hasNext();) {
            MsnSwitchboardListener listener = (MsnSwitchboardListener) iter
                    .next();
            listener.switchboardClosed(switchboard);
        }
    }

    public void fireContactJoinSwitchboard(MsnSwitchboard switchboard,
            MsnContact contact) {
        for (Iterator iter = switchboardListeners.iterator(); iter.hasNext();) {
            MsnSwitchboardListener listener = (MsnSwitchboardListener) iter
                    .next();
            listener.contactJoinSwitchboard(switchboard, contact);
        }
    }

    public void fireContactLeaveSwitchboard(MsnSwitchboard switchboard,
            MsnContact contact) {
        for (Iterator iter = switchboardListeners.iterator(); iter.hasNext();) {
            MsnSwitchboardListener listener = (MsnSwitchboardListener) iter
                    .next();
            listener.contactLeaveSwitchboard(switchboard, contact);
        }
    }

    //FileTransferListener

    public void addFileTransferListener(MsnFileTransferListener listener) {
        if (listener != null) {
            fileTransferListeners.add(listener);
        }
    }

    public void removeFileTransferListener(MsnFileTransferListener listener) {
        if (listener != null) {
            fileTransferListeners.remove(listener);
        }
    }

    public void fireFileTransferRequestReceived(MsnFileTransfer transfer) {
        for (Iterator iter = fileTransferListeners.iterator(); iter.hasNext();) {
            MsnFileTransferListener listener = (MsnFileTransferListener) iter
                    .next();
            listener.fileTransferRequestReceived(transfer);
        }
    }

    public void fireFileTransferStarted(MsnFileTransfer transfer) {
        for (Iterator iter = fileTransferListeners.iterator(); iter.hasNext();) {
            MsnFileTransferListener listener = (MsnFileTransferListener) iter
                    .next();
            listener.fileTransferStarted(transfer);
        }
    }

    public void fireFileTransferProcess(MsnFileTransfer transfer) {
        for (Iterator iter = fileTransferListeners.iterator(); iter.hasNext();) {
            MsnFileTransferListener listener = (MsnFileTransferListener) iter
                    .next();
            listener.fileTransferProcess(transfer);
        }
    }

    public void fireFileTransferFinished(MsnFileTransfer transfer) {
        for (Iterator iter = fileTransferListeners.iterator(); iter.hasNext();) {
            MsnFileTransferListener listener = (MsnFileTransferListener) iter
                    .next();
            listener.fileTransferFinished(transfer);
        }
    }

}
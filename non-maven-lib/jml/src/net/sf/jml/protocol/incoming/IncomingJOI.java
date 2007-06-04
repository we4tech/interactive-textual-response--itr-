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
package net.sf.jml.protocol.incoming;

import net.sf.jml.Email;
import net.sf.jml.MsnContactList;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.AbstractSwitchboard;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;

/**
 * When the friend which you invited have join a switchboard, 
 * you will received that notice.
 * <p>
 * Supported Protocl: All
 * <p>
 * Syntax: JOI email displayName
 * 
 * @author Roger Chen
 */
public class IncomingJOI extends MsnIncomingMessage {

    public IncomingJOI(MsnProtocol protocol) {
        super(protocol);
    }

    protected boolean isSupportTransactionId() {
        return false;
    }

    public Email getEmail() {
        return Email.parseStr(getParam(0));
    }

    public String getDisplayName() {
        return getParam(1);
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnSwitchboard switchboard = session.getSwitchboard();
        if (switchboard != null) {
            MsnMessenger messenger = session.getMessenger();
            MsnContactList contactList = messenger.getContactList();
            MsnContactImpl contact = (MsnContactImpl) contactList
                    .getContactByEmail(getEmail());
            if (contact == null) {
                contact = new MsnContactImpl(contactList);
                contact.setEmail(getEmail());
                contact.setDisplayName(getDisplayName());
            }
            ((AbstractSwitchboard) switchboard).addContact(contact);
            ((AbstractMessenger) messenger).fireContactJoinSwitchboard(
                    switchboard, contact);
        }
    }

}
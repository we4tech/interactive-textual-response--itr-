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
import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.impl.AbstractSwitchboard;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;

/**
 * When user join a switchboard, he will received the users in
 * current switchboard.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: IRO trId currentUserNumber totalUserCount email displayName
 * 
 * @author Roger Chen
 */
public class IncomingIRO extends MsnIncomingMessage {

    public IncomingIRO(MsnProtocol protocol) {
        super(protocol);
    }

    public int getCurrentNumber() {
        return NumberUtils.stringToInt(getParam(0));
    }

    public int getTotalNumber() {
        return NumberUtils.stringToInt(getParam(1));
    }

    public Email getEmail() {
        return Email.parseStr(getParam(2));
    }

    public String getDisplayName() {
        return getParam(3);
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnSwitchboard switchboard = session.getSwitchboard();
        if (switchboard != null) {
            MsnContactList contactList = session.getMessenger()
                    .getContactList();
            MsnContactImpl contact = (MsnContactImpl) contactList
                    .getContactByEmail(getEmail());
            if (contact == null) {
                contact = new MsnContactImpl(contactList);
                contact.setEmail(getEmail());
                contact.setDisplayName(getDisplayName());
            }
            ((AbstractSwitchboard) switchboard).addContact(contact);
        }
    }

}
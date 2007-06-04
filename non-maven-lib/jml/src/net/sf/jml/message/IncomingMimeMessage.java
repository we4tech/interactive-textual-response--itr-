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
package net.sf.jml.message;

import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.incoming.IncomingMSG;

/**
 * Incoming Mime Message.
 * 
 * @author Roger Chen
 */
public class IncomingMimeMessage extends IncomingMSG {

    public IncomingMimeMessage(MsnProtocol protocol) {
        super(protocol);
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        MsnMessenger messenger = session.getMessenger();
        MsnMimeMessage message = null;
        try {
            message = MsnMimeMessageFactory.parseMessage(getContent());
        } catch (Exception e) {
            ((AbstractMessenger) messenger).fireExceptionCaught(e);
        }

        if (message != null) {
            MsnContactImpl contact = (MsnContactImpl) messenger
                    .getContactList().getContactByEmail(getEmail());
            if (contact == null) {
                contact = new MsnContactImpl(messenger.getContactList());
                contact.setEmail(getEmail());
                contact.setDisplayName(getDisplayName());
            }
            message.messageReceived(session, contact);
        }
    }
}
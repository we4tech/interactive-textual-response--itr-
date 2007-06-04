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
import net.sf.jml.MsnContact;
import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnUserStatus;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;

/**
 * The notice that a friend offline.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: FLN email
 * 
 * @author Roger Chen
 */
public class IncomingFLN extends MsnIncomingMessage {

    public IncomingFLN(MsnProtocol protocol) {
        super(protocol);
    }

    protected boolean isSupportTransactionId() {
        return false;
    }

    public Email getEmail() {
        return Email.parseStr(getParam(0));
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnContact contact = session.getMessenger().getContactList()
                .getContactByEmail(getEmail());

        if (contact != null) {
            ((MsnContactImpl) contact).setStatus(MsnUserStatus.OFFLINE);
            ((AbstractMessenger) session.getMessenger())
                    .fireContactStatusChanged(contact);
        }
    }

}
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
import net.sf.jml.MsnContactList;
import net.sf.jml.MsnMessageChain;
import net.sf.jml.MsnMessageIterator;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnUserPropertyType;
import net.sf.jml.impl.MsnUserPropertiesImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.StringUtils;

/**
 * Properties, This message have relation with the previous 
 * IncomingADD/IncomingADC/IncomingLST, and can't get the 
 * properties's owner by a stand alone IncomingBPR.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: BPR propType property
 * 
 * @author Roger Chen
 */
public class IncomingBPR extends MsnIncomingMessage {

    public IncomingBPR(MsnProtocol protocol) {
        super(protocol);
    }

    protected boolean isSupportTransactionId() {
        return false;
    }

    public MsnUserPropertyType getPropType() {
        return MsnUserPropertyType.parseStr(getParam(0));
    }

    public String getProperty() {
        return StringUtils.urlDecode(getParam(1));
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        MsnMessenger messenger = session.getMessenger();
        MsnContactList contactList = messenger.getContactList();
        MsnMessageChain incoming = messenger.getIncomingMessageChain();
        MsnContact contact = null;

        for (MsnMessageIterator iter = incoming.iterator(); iter.hasPrevious();) {
            MsnMessage message = iter.previous();
            if (message instanceof IncomingBPR)
                continue;
            if (message instanceof IncomingLST) {
                Email email = ((IncomingLST) message).getEmail();
                contact = contactList.getContactByEmail(email);
            } else if (message instanceof IncomingADC) {
                Email email = ((IncomingADC) message).getEmail();
                if (email == null) {
                    contact = contactList
                            .getContactById(((IncomingADC) message).getId());
                } else {
                    contact = contactList.getContactByEmail(email);
                }
            } else if (message instanceof IncomingADD) {
                Email email = ((IncomingADD) message).getEmail();
                contact = contactList.getContactByEmail(email);
            }
            break;
        }

        if (contact != null) {
            ((MsnUserPropertiesImpl) contact.getProperties()).setProperty(
                    getPropType(), getProperty());
        }
    }
}
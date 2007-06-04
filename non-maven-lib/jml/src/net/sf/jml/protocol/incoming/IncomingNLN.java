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
import net.sf.jml.MsnClientId;
import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnUserStatus;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * The notice message that friends change their status.
 * <p>
 * Supported Protocol: All
 * <p>
 * MSNP8 Syntax: NLN userStatus email displayName clientId
 * <p>
 * MSNP9/MSNP10 Syntax: NLN userStatus email displayName clientId msnObject
 * 
 * @author Roger Chen
 */
public class IncomingNLN extends MsnIncomingMessage {

    public IncomingNLN(MsnProtocol protocol) {
        super(protocol);
    }

    protected boolean isSupportTransactionId() {
        return false;
    }

    public MsnUserStatus getUserStatus() {
        return MsnUserStatus.parseStr(getParam(0));
    }

    public Email getEmail() {
        return Email.parseStr(getParam(1));
    }

    public String getDisplayName() {
        return StringUtils.urlDecode(getParam(2));
    }

    public int getClientId() {
        return NumberUtils.stringToInt(getParam(3));
    }

    public String getMsnObject() {
        String object = getParam(4);
        if (object != null) {
            return StringUtils.urlDecode(object);
        }
        return null;
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        MsnContactImpl contact = (MsnContactImpl) session.getMessenger()
                .getContactList().getContactByEmail(getEmail());

        if (contact != null) {
            contact.setDisplayName(getDisplayName());
            contact.setClientId(MsnClientId.parseInt(getClientId()));
            contact.setStatus(getUserStatus());

            ((AbstractMessenger) session.getMessenger())
                    .fireContactStatusChanged(contact);
        }
    }

}
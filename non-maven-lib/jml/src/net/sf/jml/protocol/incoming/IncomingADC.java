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
import net.sf.jml.MsnList;
import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.impl.MsnContactListImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.StringUtils;

/**
 * Add a friend response or other user add you notice. In MSNP10, 
 * it's the response of OutgoingADC or other user add you notice.
 * In MSNP8/MSNP9, it should be IncomingADD message.
 * <p>
 * Supported Protocol: MSNP10/MSNP11
 * <p>
 * Syntax 1: ADC trId AL|BL N=email
 * <p>
 * Syntax 2: ADC trId FL N=email F=friendlyName C=id
 * <p>
 * Syntax 3: ADC trId FL C=id groupId
 * <p>
 * Syntax 4: ADC 0 RL N=email F=displayName
 * <p>
 * Syntax 1/2/3 is the response of OutgoingADC, Syntax 4 is the 
 * notice that other user add you.
 * 
 * @author Roger Chen
 */
public class IncomingADC extends MsnIncomingMessage {

    public IncomingADC(MsnProtocol protocol) {
        super(protocol);
    }

    public MsnList getList() {
        return MsnList.parseStr(getParam(0));
    }

    private int getSyntaxType() {
        MsnList list = getList();
        if (list == MsnList.RL)
            return 4;
        if (list == MsnList.FL) {
            if (getParamCount() == 4)
                return 2;
            return 3;
        }
        return 1;
    }

    public String getId() {
        switch (getSyntaxType()) {
        case 2:
            return getParam(3).substring(2);
        case 3:
            return getParam(1).substring(2);
        default:
            return null;
        }
    }

    public Email getEmail() {
        switch (getSyntaxType()) {
        case 1:
        case 2:
        case 4:
            return Email.parseStr(getParam(1).substring(2));
        default:
            return null;
        }
    }

    public String getFriendlyName() {
        switch (getSyntaxType()) {
        case 2:
            return StringUtils.urlDecode(getParam(2)).substring(2);
        default:
            return null;
        }
    }

    public String getGroupId() {
        switch (getSyntaxType()) {
        case 3:
            return getParam(2);
        default:
            return null;
        }
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        MsnContactListImpl contactList = (MsnContactListImpl) session
                .getMessenger().getContactList();
        MsnList list = getList();
        MsnContactImpl contact = null;

        int syntaxType = getSyntaxType();
        switch (syntaxType) {
        case 1:
            contact = (MsnContactImpl) contactList
                    .getContactByEmail(getEmail());
            if (contact != null)
                contact.setInList(list, true);
            break;
        case 3:
            contact = (MsnContactImpl) contactList.getContactById(getId());
            if (contact != null)
                contact.addBelongGroup(getGroupId());
            break;
        case 2:
        case 4:
            contact = (MsnContactImpl) contactList
                    .getContactByEmail(getEmail());
            if (contact == null) {
                contact = new MsnContactImpl(contactList);
                contact.setEmail(getEmail());
                contact.setFriendlyName(getFriendlyName());
                contact.setDisplayName(contact.getFriendlyName());
                contactList.addContact(contact);
            }
            if (syntaxType == 2) {
                contact.setId(getId());
                contact.setInList(MsnList.FL, true);
                ((AbstractMessenger) session.getMessenger())
                        .fireContactAddCompleted(contact);
            } else {
                contact.setInList(MsnList.RL, true);
                ((AbstractMessenger) session.getMessenger())
                        .fireContactAddedMe(contact);
            }
            break;
        }
    }

}
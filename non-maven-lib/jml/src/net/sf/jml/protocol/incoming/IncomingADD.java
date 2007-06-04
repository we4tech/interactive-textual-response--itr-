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
 * Add a friend response or other user add you notice. In MSNP8/MSNP9, 
 * it's the response of OutgoingADC or other user add you notice. 
 * In MSNP10, it should be IncomingADC message.
 * <p>
 * Supported Protocol: MSNP8/MSNP9
 * <p>
 * Syntax 1: ADD trId AL|BL|FL versionNum email friendlyName( groupId)
 * <p>
 * Syntax 2: ADD 0 RL versionNum email displayName
 * <p>
 * Syntax 1 is the response of OutgoingADD, Syntax 2 is the 
 * notice that other user add you.
 * 
 * @author Roger Chen
 */
public class IncomingADD extends MsnIncomingMessage {

    public IncomingADD(MsnProtocol protocol) {
        super(protocol);
    }

    public MsnList getList() {
        return MsnList.parseStr(getParam(0));
    }

    public Email getEmail() {
        return Email.parseStr(getParam(2));
    }

    public String getFriendlyName() {
        return StringUtils.urlDecode(getParam(3));
    }

    public String getGroupId() {
        return getParam(4);
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        MsnContactListImpl contactList = (MsnContactListImpl) session
                .getMessenger().getContactList();
        Email email = getEmail();
        MsnList list = getList();

        //    Update contact list
        MsnContactImpl contact = (MsnContactImpl) contactList
                .getContactByEmail(email);
        if (contact == null) {
            contact = new MsnContactImpl(contactList);
            contact.setEmail(email);
            contact.setFriendlyName(getFriendlyName());
            contact.setDisplayName(contact.getFriendlyName());
            contactList.addContact(contact);
        }
        contact.setInList(list, true);

        //Added to FL, so need to added to corresponding group.
        if (list == MsnList.FL) {
            contact.addBelongGroup(getGroupId());
        } else if (list == MsnList.RL) {
            ((AbstractMessenger) session.getMessenger())
                    .fireContactAddedMe(contact);
        }
    }

}
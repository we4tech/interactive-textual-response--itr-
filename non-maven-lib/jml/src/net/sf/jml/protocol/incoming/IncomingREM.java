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
import net.sf.jml.util.NumberUtils;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.impl.MsnContactListImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;

/**
 * The notice message of other user remove you from his contact 
 * list or the response message of OutgoingREM.
 * <p>
 * Supported Protocol: All
 * <p>
 * MSNP8/MSNP9 Syntax 1: REM trId AL|BL|FL versionNum id( groupId)
 * <p>
 * MSNP8/MSNP9 Syntax 2: REM 0 RL versionNum email
 * <p>
 * MSNP10 Syntax 1: REM trId AL|BL|FL id( groupId)
 * <p>
 * MSNP10 Syntax 2: REM 0 RL email
 * <p>
 * Syntax 1 is the response of OutgoingREM. For example: REM 32 FL 1203 principal@msn.com 1
 * <p>
 * Syntax 2 is the notice when other user remove you. For example: REM 0 RL 3050 example@passport.com
 * 
 * @author Roger Chen
 */
public class IncomingREM extends MsnIncomingMessage {

    public IncomingREM(MsnProtocol protocol) {
        super(protocol);
    }

    public MsnList getList() {
        return MsnList.parseStr(getParam(0));
    }

    public Email getEmail() {
        if (getList() == MsnList.RL) {
            if (protocol.before(MsnProtocol.MSNP10)) {
                return Email.parseStr(getParam(2));
            }
            return Email.parseStr(getParam(1));
        }
        else {
            return null;
        }
    }

    public int getVersion() {
        if (protocol.before(MsnProtocol.MSNP10)) {
            return NumberUtils.stringToInt(getParam(1));
        }
        return -1;
    }

    public String getId() {
        if (protocol.before(MsnProtocol.MSNP10)) {
            return getEmail().getEmailAddress();
        }
        return getParam(1);
    }

    public String getGroupId() {
        if (protocol.before(MsnProtocol.MSNP10)) {
            return getParam(3);
        }
        return getParam(2);
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        MsnContactListImpl contactList = (MsnContactListImpl) session
                .getMessenger().getContactList();
        MsnList list = getList();

        //   Update contact list
        if (list == MsnList.RL) {
            MsnContactImpl contact = (MsnContactImpl) contactList
                    .getContactByEmail(getEmail());
            if (contact != null) {
                contact.setInList(MsnList.RL, false);
                ((AbstractMessenger) session.getMessenger())
                        .fireContactRemovedMe(contact);
            }
        } else {
            MsnContactImpl contact;
            if (list == MsnList.AL) {
                contact = (MsnContactImpl) contactList.getContactByEmail(getEmail());
            }
            else {
                contact = (MsnContactImpl) contactList.getContactById(getId());
            }
            if (contact != null) {
                contact.setInList(list, false);
                if (contact.getListNumber() == 0) { //Not in any group, delete from contact list
                    contactList.removeContactByEmail(contact.getEmail());
                }
                if (list == MsnList.FL) { //In FL, remove user from the group.
                    contact.removeBelongGroup(getGroupId());
                    ((AbstractMessenger) session.getMessenger())
                            .fireContactRemoveCompleted(contact);
                }
            }
        }
    }

}
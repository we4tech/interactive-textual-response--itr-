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

import net.sf.jml.*;
import net.sf.jml.util.StringUtils;
import net.sf.jml.protocol.outgoing.*;

/**
 * Simple msn messenger.
 * 
 * @author Roger Chen
 */
public class SimpleMessenger extends BasicMessenger {

    SimpleMessenger(Email email, String password) {
        super(email, password);
    }

    public void newSwitchboard(Object attachment) {
        OutgoingXFR outgoing = new OutgoingXFR(getActualMsnProtocol());
        outgoing.setAttachment(attachment);
        send(outgoing);
    }

    public void addGroup(String groupName) {
        if (groupName == null)
            return;
        OutgoingADG message = new OutgoingADG(getActualMsnProtocol());
        message.setGroupName(groupName);
        send(message);
    }

    public void removeGroup(String groupId) {
        MsnGroup group = getContactList().getGroup(groupId);
        if (group != null) {
            OutgoingRMG message = new OutgoingRMG(getActualMsnProtocol());
            message.setGroupId(groupId);
            send(message);
        }
    }

    public void renameGroup(String groupId, String newGroupName) {
        if (groupId == null || newGroupName == null)
            return;
        MsnGroup group = getContactList().getGroup(groupId);
        if (group != null && !group.isDefaultGroup()
                && !group.getGroupName().equals(newGroupName)) {
            OutgoingREG message = new OutgoingREG(getActualMsnProtocol());
            message.setGroupId(groupId);
            message.setGroupName(newGroupName);
            send(message);
        }
    }

    private void addFriend(MsnList list, Email email, String friendlyName) {
        if (list == null || email == null || list == MsnList.RL
                || list == MsnList.PL)
            return;
        MsnContact contact = getContactList().getContactByEmail(email);
        if (contact.isInList(list)){
            return;
        }

        MsnProtocol protocol = getActualMsnProtocol();
        if (protocol.after(MsnProtocol.MSNP9)) {
            OutgoingADC message = new OutgoingADC(protocol);
            message.setAddtoList(list);
            message.setEmail(email);
            if (list == MsnList.FL)
                message.setFriendlyName(friendlyName == null ? email
                        .getEmailAddress() : friendlyName);
            send(message);
        } else {
            OutgoingADD message = new OutgoingADD(protocol);
            message.setAddtoList(list);
            message.setEmail(email);
            message.setFriendlyName(friendlyName == null ? email
                    .getEmailAddress() : friendlyName);
            send(message);
        }
    }

    private void removeFriend(MsnList list, Email email, String id,
            String groupId) {
        if (list == null || list == MsnList.RL)
            return;
        if (list == MsnList.FL) {
            if (id == null)
                return;
        } else if (email == null)
            return;
        MsnContact contact = getContactList().getContactByEmail(email);
        if (!contact.isInList(list)){
            return;
        }

        OutgoingREM message = new OutgoingREM(getActualMsnProtocol());
        message.setRemoveFromList(list);
        if (list == MsnList.FL) {
            message.setId(id);
            if (groupId != null)
                message.setGroupId(groupId);
        } else {
            message.setEmail(email);
        }
        send(message);
    }

    public void addFriend(Email email, String friendlyName) {
        if (email == null)
            return;
        if (friendlyName == null)
            friendlyName = email.getEmailAddress();
        MsnContact contact = getContactList().getContactByEmail(email);
        if (contact != null) {
            if (!contact.isInList(MsnList.FL)) {
                addFriend(MsnList.FL, email, friendlyName);
            }
            if (!contact.isInList(MsnList.AL)) {
                addFriend(MsnList.AL, email, friendlyName);
            }
        }
        else {
            addFriend(MsnList.FL, email, friendlyName);
            addFriend(MsnList.AL, email, friendlyName);
        }
    }

    public void blockFriend(Email email) {
        removeFriend(MsnList.AL, email, null, null);
        addFriend(MsnList.BL, email, null);
    }

    public void copyFriend(Email email, String groupId) {
        MsnContact contact = getContactList().getContactByEmail(email);
        MsnGroup group = getContactList().getGroup(groupId);
        if (contact == null || group == null || group.isDefaultGroup())
            return;

        MsnProtocol protocol = getActualMsnProtocol();
        if (protocol.after(MsnProtocol.MSNP9)) {
            OutgoingADC outgoing = new OutgoingADC(protocol);
            outgoing.setAddtoList(MsnList.FL);
            outgoing.setId(contact.getId());
            outgoing.setGroupId(groupId);
            send(outgoing);
        } else {
            OutgoingADD outgoing = new OutgoingADD(protocol);
            outgoing.setAddtoList(MsnList.FL);
            outgoing.setEmail(email);
            outgoing.setFriendlyName(contact.getFriendlyName());
            outgoing.setGroupId(groupId);
            send(outgoing);
        }
    }

    public void moveFriend(Email email, String srcGroupId, String destGroupId) {
        if (email == null)
            return;
        MsnGroup srcGroup = getContactList().getGroup(srcGroupId);
        MsnGroup destGroup = getContactList().getGroup(destGroupId);
        if (srcGroup == null || destGroup == null || srcGroup.equals(destGroup))
            return;

        if (destGroup.isDefaultGroup()) { //move to default group
            removeFriend(email, srcGroupId);
        } else {
            copyFriend(email, destGroupId);
            if (!srcGroup.isDefaultGroup()) //not move from default group
                removeFriend(email, srcGroupId);
        }
    }

    public void removeFriend(Email email, boolean block) {
        if (email == null)
            return;
        MsnContact contact = getContactList().getContactByEmail(email);
        if (contact != null) {
            removeFriend(MsnList.FL, email, contact.getId(), null);
            if (block) {
                blockFriend(email);
            }   
        }   
    }

    public void removeFriend(Email email, String groupId) {
        if (email == null)
            return;
        MsnContact contact = getContactList().getContactByEmail(email);
        if (contact != null) {
            removeFriend(MsnList.FL, email, contact.getId(), groupId);
        }
    }

    public void renameFriend(Email email, String friendlyName) {
        if (email == null || friendlyName == null)
            return;
        MsnContact contact = getContactList().getContactByEmail(email);
        if (contact != null) {
            MsnProtocol protocol = getActualMsnProtocol();
            if (protocol.after(MsnProtocol.MSNP10)) {
                OutgoingSBP message = new OutgoingSBP(getActualMsnProtocol());
                message.setId(contact.getId());
                message.setPropertyType(MsnUserPropertyType.MFN);
                message.setProperty(StringUtils.urlEncode(friendlyName));
                send(message);
            }
            else {
                OutgoingREA message = new OutgoingREA(getActualMsnProtocol());
                message.setId(contact.getEmail().getEmailAddress());
                message.setFriendlyName(StringUtils.urlEncode(friendlyName));
                send(message);
            }
        }
    }

    public void unblockFriend(Email email) {
        removeFriend(MsnList.BL, email, null, null);
        addFriend(MsnList.AL, email, null);
    }
}

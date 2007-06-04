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

import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.MsnContactListImpl;
import net.sf.jml.impl.MsnGroupImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.StringUtils;

/**
 * One of responses of OutgoingSYN, return groups. MSNP8/MSNP9
 * will return the default group, and MSNP10 or higher will not. 
 * <p>
 * Supported Protocol: All
 * <p>
 * MSNP8/MSNP9 Syntax: LSG groupId groupName 0
 * <p>
 * MSNP10 Syntax: LSG groupName groupId
 * 
 * @author Roger Chen
 */
public class IncomingLSG extends MsnIncomingMessage {

    public IncomingLSG(MsnProtocol protocol) {
        super(protocol);
    }

    protected boolean isSupportTransactionId() {
        return false;
    }

    public String getGroupId() {
        if (protocol.after(MsnProtocol.MSNP9)) {
            return getParam(1);
        }
        String groupId = getParam(0);
        if (groupId.equals("0")) { //In MSNP8/MSNP9, 0 is the default group
            return null;
        }
        return groupId;
    }

    public String getGroupName() {
        if (protocol.after(MsnProtocol.MSNP9)) {
            return StringUtils.urlDecode(getParam(0));
        }
        return StringUtils.urlDecode(getParam(1));
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        MsnContactListImpl contactList = (MsnContactListImpl) session
                .getMessenger().getContactList();
        String groupId = getGroupId();

        if (groupId != null) {
            MsnGroupImpl group = new MsnGroupImpl(contactList);
            group.setGroupId(groupId);
            group.setGroupName(getGroupName());
            contactList.addGroup(group);
        } else { //default group
            ((MsnGroupImpl) contactList.getDefaultGroup())
                    .setGroupName(getGroupName());
        }

        if (contactList.getCurrentContactCount() == contactList
                .getContactCount()
                && contactList.getCurrentGroupCount() == contactList
                        .getGroupCount()) {
            ((AbstractMessenger) session.getMessenger())
                    .fireContactListSyncCompleted(); //Sync completed
        }
    }

}
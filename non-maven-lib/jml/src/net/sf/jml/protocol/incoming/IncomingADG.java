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
import net.sf.jml.impl.MsnContactListImpl;
import net.sf.jml.impl.MsnGroupImpl;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * OutgoingADG's response message, indicate add a group success.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: ADG trId versionNum groupName groupId
 * 
 * @author Roger Chen
 */
public class IncomingADG extends MsnIncomingMessage {

    public IncomingADG(MsnProtocol protocol) {
        super(protocol);
    }

    public int getVersion() {
        return NumberUtils.stringToInt(getParam(0));
    }

    public String getGroupName() {
        if (protocol.before(MsnProtocol.MSNP10)) {
            return StringUtils.urlDecode(getParam(1));
        }
        else {
            return StringUtils.urlDecode(getParam(0));
        }
    }

    public String getGroupId() {
        if (protocol.before(MsnProtocol.MSNP10)) {
            return getParam(2);
        }
        else {
            return getParam(1);
        }
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        MsnContactListImpl contactList = (MsnContactListImpl) session
                .getMessenger().getContactList();
        MsnGroupImpl group = new MsnGroupImpl(contactList);
        group.setGroupId(getGroupId());
        group.setGroupName(getGroupName());
        contactList.addGroup(group);

        ((AbstractMessenger) session.getMessenger())
                .fireGroupAddCompleted(group);
    }

}
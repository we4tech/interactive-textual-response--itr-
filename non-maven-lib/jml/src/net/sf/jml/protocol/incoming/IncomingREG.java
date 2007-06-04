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

import net.sf.jml.MsnContactList;
import net.sf.jml.MsnGroup;
import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.MsnGroupImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * The response of OutgoingREG, indicate the group name has 
 * changed successfully.
 * <p>
 * Supported Protocl: All
 * <p>
 * Syntax: REG trId versionNum groupId groupName
 * 
 * @author Roger Chen
 */
public class IncomingREG extends MsnIncomingMessage {

    public IncomingREG(MsnProtocol protocol) {
        super(protocol);
    }

    public int getVersion() {
        return NumberUtils.stringToInt(getParam(0));
    }

    public String getGroupId() {
        if (protocol.before(MsnProtocol.MSNP10)) {
            return getParam(1);
        }
        else {
            return getParam(0);
        }
    }

    public String getGroupName() {
        if (protocol.before(MsnProtocol.MSNP10)) {
            return StringUtils.urlDecode(getParam(2));
        }
        else {
            return StringUtils.urlDecode(getParam(1));
        }
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnContactList contactList = session.getMessenger().getContactList();
        MsnGroup group = contactList.getGroup(getGroupId());
        if (group != null)
            ((MsnGroupImpl) group).setGroupName(getGroupName());
    }

}
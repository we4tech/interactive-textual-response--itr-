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

/**
 * The response message of OutgoingRMG, indicate delete the
 * group successfully.
 * <p>
 * Supported Protocol: All
 * <p>
 * MSNP8/MSNP9 Syntax: RMG trId versionNum groupId
 * <p>
 * MSNP10 Syntax: RMG trId groupId
 * 
 * @author Roger Chen
 */
public class IncomingRMG extends MsnIncomingMessage {

    public IncomingRMG(MsnProtocol protocol) {
        super(protocol);
    }

    public int getVersion() {
        return NumberUtils.stringToInt(getParam(0));
    }

    public String getGroupId() {
        if (protocol.before(MsnProtocol.MSNP10)) {
            return getParam(1);
        }
        return getParam(0);
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnContactListImpl contactList = (MsnContactListImpl) session
                .getMessenger().getContactList();
        MsnGroupImpl group = (MsnGroupImpl) contactList.getGroup(getGroupId());

        if (group != null) {
            group.clear();
            contactList.removeGroup(getGroupId());

            ((AbstractMessenger) session.getMessenger())
                    .fireGroupRemoveCompleted(group);
        }
    }

}
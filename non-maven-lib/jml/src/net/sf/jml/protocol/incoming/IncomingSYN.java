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

import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.MsnContactListImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.outgoing.OutgoingCHG;
import net.sf.jml.util.NumberUtils;

/**
 * The response of OutgoingSYN, indicate the contact list size
 * and group count.
 * <p>
 * Supported Protocl: All
 * <p>
 * MSNP8/MSNP9 Syntax: SYN trId currentVersion( contactCount groupCount)
 * <p>
 * MSNP10 Syntax: SYN trId lastTimeChanged lastBeChangedTime( contactCount groupCount)
 * 
 * @author Roger Chen
 */
public class IncomingSYN extends MsnIncomingMessage {

    public IncomingSYN(MsnProtocol protocol) {
        super(protocol);
    }

    public String getVersion() {
        if (protocol.before(MsnProtocol.MSNP10)) {
            return getParam(0);
        } else {
            return getParam(0) + " " + getParam(1);
        }
    }

    public int getContactCount() {
        String contactCount = null;
        if (protocol.before(MsnProtocol.MSNP10)) {
            contactCount = getParam(1);
        } else {
            contactCount = getParam(2);
        }
        if (contactCount == null) {
            return -1;
        }
        return NumberUtils.stringToInt(contactCount);
    }

    public int getGroupCount() {
        String groupCount = null;
        if (protocol.before(MsnProtocol.MSNP10)) {
            groupCount = getParam(2);
        } else {
            groupCount = getParam(3);
        }
        if (groupCount == null) {
            return -1;
        }
        return NumberUtils.stringToInt(groupCount);
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        MsnMessenger messenger = session.getMessenger();
        MsnContactListImpl contactList = (MsnContactListImpl) messenger
                .getContactList();
        String version = getVersion();

        if (version.equals(contactList.getVersion())) {
            ((AbstractMessenger) messenger).fireContactListSyncCompleted();
        } else {
            int groupCount = getGroupCount();
            if (!protocol.before(MsnProtocol.MSNP10)) {
                groupCount++; //In MSNP8/MSNP9 will return default group 
            }
            contactList.setVersion(version);
            contactList.setGroupCount(groupCount);
            contactList.setContactCount(getContactCount());
        }

        OutgoingCHG message = new OutgoingCHG(protocol);
        message.setStatus(messenger.getOwner().getInitStatus());
        message.setClientId(messenger.getOwner().getClientId());
        message.setDisplayPicture(messenger.getOwner().getDisplayPicture());
        message.setFirstSend(true);
        messenger.send(message);
    }

}
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

import net.sf.jml.MsnContact;
import net.sf.jml.MsnContactList;
import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.impl.MsnOwnerImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * The response of OutgoingREA, indicate the user has changed
 * the name successfully.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: REA trId id friendlyName
 * 
 * @author Roger Chen
 */
public class IncomingREA extends MsnIncomingMessage {

    public IncomingREA(MsnProtocol protocol) {
        super(protocol);
    }

    public int getVersion() {
        return NumberUtils.stringToInt(getParam(0));
    }

    public String getId() {
        return getParam(1);
    }

    public String getFriendlyName() {
        return StringUtils.urlDecode(getParam(2));
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnContactList contactList = session.getMessenger().getContactList();
        if (session.getMessenger().getOwner().getEmail().toString().equals(getId())) {
            MsnOwnerImpl owner = (MsnOwnerImpl) session.getMessenger().getOwner();
            owner.fSetDisplayName(getFriendlyName());
        }
        else {
            MsnContact contact = contactList.getContactById(getId());
            if (contact != null) {
                ((MsnContactImpl) contact).setFriendlyName(getFriendlyName());
            }
        }
    }

}
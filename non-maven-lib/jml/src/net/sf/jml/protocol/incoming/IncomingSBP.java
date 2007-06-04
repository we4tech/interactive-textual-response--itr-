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
import net.sf.jml.MsnUserPropertyType;
import net.sf.jml.MsnContactList;
import net.sf.jml.MsnContact;
import net.sf.jml.impl.MsnUserPropertiesImpl;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.StringUtils;

/**
 * The response message of OutgoingSBP, indicating a change to a contact's properties.
 * <p>
 * Supported Protocol: >= MSNP11
 * <p>
 * Syntax: SBP trId id propType property
 *
 * @author Daniel Henninger
 */
public class IncomingSBP extends MsnIncomingMessage {

    public IncomingSBP(MsnProtocol protocol) {
        super(protocol);
    }

    public String getId() {
        return getParam(0);
    }

    public MsnUserPropertyType getPropertyType() {
        return MsnUserPropertyType.parseStr(getParam(1));
    }

    public String getProperty() {
        return StringUtils.urlDecode(getParam(2));
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnContactList contactList = session.getMessenger().getContactList();
        MsnContact contact = contactList.getContactById(getId());
        if (contact != null) {
            String prop = getProperty();
            MsnUserPropertyType propType = getPropertyType();
            MsnUserPropertiesImpl properties = (MsnUserPropertiesImpl) contact.getProperties();
            properties.setProperty(propType, prop);

            if (propType.equals(MsnUserPropertyType.MFN)) {
                ((MsnContactImpl) contact).setFriendlyName(prop);
            }
        }

    }

}
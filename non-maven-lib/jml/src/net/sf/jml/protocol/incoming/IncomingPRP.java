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
import net.sf.jml.impl.MsnUserPropertiesImpl;
import net.sf.jml.impl.MsnOwnerImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * The response message of OutgoingPRP or one of the responses 
 * message of OutgoingSYN, indicate your properties.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax 1: PRP propType property
 * <p>
 * Syntax 2: PRP trId versionNum propType property
 * <p>
 * Syntax 1 is the response of OutgoingSYN. For example: PRP PHH 01%20234
 * <p>
 * Syntax 2 is the response of OutgoingPRP. For example: PRP 55 12183 PHH 555-1234
 * 
 * @author Roger Chen
 */
public class IncomingPRP extends MsnIncomingMessage {

    public IncomingPRP(MsnProtocol protocol) {
        super(protocol);
    }

    public int getVersion() {
        if (getTransactionId() >= 0) {
            return NumberUtils.stringToInt(getParam(0));
        }
        return -1;
    }

    public MsnUserPropertyType getPropertyType() {
        if (getTransactionId() >= 0) {
            return MsnUserPropertyType.parseStr(getParam(1));
        } else {
            return MsnUserPropertyType.parseStr(getParam(0));
        }
    }

    public String getProperty() {
        if (getTransactionId() >= 0) {
            return StringUtils.urlDecode(getParam(2));
        } else {
            return StringUtils.urlDecode(getParam(1));
        }
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        String prop = getProperty();
        MsnUserPropertyType propType = getPropertyType();
        if (propType != null && prop != null) {
            MsnUserPropertiesImpl properties = (MsnUserPropertiesImpl) session
                    .getMessenger().getOwner().getProperties();
            properties.setProperty(propType, prop);

            if (propType.equals(MsnUserPropertyType.MFN)) {
                MsnOwnerImpl owner = (MsnOwnerImpl) session.getMessenger().getOwner();
                owner.fSetDisplayName(prop);
            }
        }
    }

}
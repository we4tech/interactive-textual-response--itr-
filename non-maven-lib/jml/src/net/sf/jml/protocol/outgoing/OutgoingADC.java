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
package net.sf.jml.protocol.outgoing;

import net.sf.jml.Email;
import net.sf.jml.MsnList;
import net.sf.jml.MsnProtocol;
import net.sf.jml.protocol.MsnOutgoingMessage;

/**
 * Add user to contact list. In MSNP8/MSNP9 is OutgoingADD.
 * <p>
 * Supported Protocol: MSNP10
 * <p>
 * Syntax 1: ADC trId AL|BL N=email
 * <p>
 * Syntax 2: ADC trId FL N=email F=friendlyName
 * <p>
 * Syntax 3: ADC trId FL C=id groupId
 * <p>
 * Can't add user to RL, can't set email and id both in a message,
 * and can't set friendlyName and groupId both in a message.
 * 
 * @author Roger Chen
 */
public class OutgoingADC extends MsnOutgoingMessage {

    public OutgoingADC(MsnProtocol protocol) {
        super(protocol);
        setCommand("ADC");
    }

    public void setAddtoList(MsnList list) {
        if (list == null) {
            throw new NullPointerException();
        }
        if (list == MsnList.RL) {
            throw new IllegalArgumentException(list.toString());
        }
        setParam(0, list.getListName());
    }

    public void setEmail(Email email) {
        setParam(1, "N=" + email.getEmailAddress());
    }

    public void setId(String id) {
        setParam(1, "C=" + id);
    }

    public void setFriendlyName(String friendlyName) {
        setParam(2, "F=" + friendlyName);
    }

    public void setGroupId(String groupId) {
        if (groupId != null) {
            setParam(2, groupId);
        }
    }
}
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
 * Add user to contact list. In MSNP8/MSNP9 is OutgoingADC.
 * <p>
 * Supported Protocol: MSNP8/MSNP9
 * <p>
 * Syntax 1: ADD trId AL|BL|FL email friendlyName
 * <p>
 * Syntax 2: ADD trId FL email friendlyName groupId
 * <p>
 * Can't add user to RL.
 * 
 * @author Roger Chen
 */
public class OutgoingADD extends MsnOutgoingMessage {

    public OutgoingADD(MsnProtocol protocol) {
        super(protocol);
        setCommand("ADD");
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
        setParam(1, email.toString());
    }

    public void setFriendlyName(String friendlyName) {
        setParam(2, friendlyName);
    }

    public void setGroupId(String groupId) {
        if (groupId != null) {
            setParam(3, groupId);
        }
    }

}
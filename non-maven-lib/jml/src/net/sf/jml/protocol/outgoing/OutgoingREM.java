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
 * Remove a user from contact list.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax 1: REM trId AL|BL email
 * <p>
 * Syntax 2: REM trId FL id( groupId)
 * <p>
 * Can't remove a user from RL, can't set email and id both in one 
 * message.
 * 
 * @author Roger Chen
 */
public class OutgoingREM extends MsnOutgoingMessage {

    public OutgoingREM(MsnProtocol protocol) {
        super(protocol);
        setCommand("REM");
    }

    public void setRemoveFromList(MsnList list) {
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

    public void setId(String id) {
        setParam(1, id);
    }

    public void setGroupId(String groupId) {
        if (groupId != null) {
            setParam(2, groupId);
        }
    }

}
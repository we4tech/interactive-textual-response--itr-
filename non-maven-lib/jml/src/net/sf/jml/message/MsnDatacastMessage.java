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
package net.sf.jml.message;

import net.sf.jml.MsnContact;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.protocol.MsnSession;

/**
 * the message which contentType is CT_DATACAST. For example, Nudge.
 * 
 * @author Roger Chen
 */
public class MsnDatacastMessage extends MsnPropMessage {

    private static final String KEY_ID = "ID";

    public MsnDatacastMessage() {
        setContentType(MessageConstants.CT_DATACAST);
    }

    public int getId() {
        return properties.getIntProperty(KEY_ID);
    }

    public void setId(int id) {
        properties.setProperty(KEY_ID, id);
    }

    protected void messageReceived(MsnSession session, MsnContact contact) {
        super.messageReceived(session, contact);

        ((AbstractMessenger) session.getMessenger())
                .fireDatacastMessageReceived(session.getSwitchboard(), this,
                        contact);
    }
}
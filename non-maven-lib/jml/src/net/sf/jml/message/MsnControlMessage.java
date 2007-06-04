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
import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.outgoing.OutgoingMSG;

/**
 * Msn typing message, indicate a user is typing message.
 * 
 * @author Roger Chen
 */
public class MsnControlMessage extends MsnPropMessage {

    private static final String KEY_TYPING_USER = "TypingUser";
    private static final String KEY_RECORDING_USER = "RecordingUser";

    public MsnControlMessage() {
        setContentType(MessageConstants.CT_CONTROL);
    }

    public String getTypingUser() {
        return headers.getProperty(KEY_TYPING_USER);
    }

    public void setTypingUser(String typingUser) {
        headers.setProperty(KEY_TYPING_USER, typingUser);
    }

    public String getRecordingUser() {
        return headers.getProperty(KEY_RECORDING_USER);
    }

    public void setRecordingUser(String recordingUser) {
        headers.setProperty(KEY_RECORDING_USER, recordingUser);
    }

    public OutgoingMSG[] toOutgoingMsg(MsnProtocol protocol) {
        OutgoingMSG[] messages = super.toOutgoingMsg(protocol);
        if (messages != null)
            for (int i = 0; i < messages.length; i++) {
                messages[i].setMsgType(OutgoingMSG.TYPE_ACKNOWLEDGE_NONE);
            }
        return messages;
    }

    protected void messageReceived(MsnSession session, MsnContact contact) {
        super.messageReceived(session, contact);

        ((AbstractMessenger) session.getMessenger())
                .fireControlMessageReceived(session.getSwitchboard(), this,
                        contact);
    }

}
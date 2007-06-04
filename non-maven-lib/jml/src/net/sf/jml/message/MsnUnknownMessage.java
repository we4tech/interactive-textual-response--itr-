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

import java.nio.ByteBuffer;

import net.sf.jml.MsnContact;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.Charset;

/**
 * Unknown message, the message that jml not support.
 * 
 * @author Roger Chen
 */
public final class MsnUnknownMessage extends MsnMimeMessage {

    private byte[] content;

    MsnUnknownMessage() {
    }

    protected void parseBuffer(ByteBuffer buffer) {
        super.parseBuffer(buffer);
        content = new byte[buffer.remaining()];
        buffer.get(content);
    }

    public byte[] getContent() {
        return content;
    }

    public String getContentAsString() {
        return Charset.decode(content);
    }

    protected void messageReceived(MsnSession session, MsnContact contact) {
        super.messageReceived(session, contact);

        ((AbstractMessenger) session.getMessenger())
                .fireUnknownMessageReceived(session.getSwitchboard(), this,
                        contact);
    }
}
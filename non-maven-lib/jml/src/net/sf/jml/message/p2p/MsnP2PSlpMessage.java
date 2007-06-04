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
package net.sf.jml.message.p2p;

import java.nio.ByteBuffer;

import net.sf.jml.MsnContact;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.msnslp.MsnslpMessage;
import net.sf.jml.protocol.msnslp.MsnslpRequest;
import net.sf.jml.util.Charset;
import net.sf.jml.util.StringUtils;
import net.sf.jml.MsnObject;

/**
 * @author Roger Chen
 */
public class MsnP2PSlpMessage extends MsnP2PMessage {

    private MsnslpMessage slpMessage;

    public MsnslpMessage getSlpMessage() {
        return slpMessage;
    }

    public void setSlpMessage(MsnslpMessage slpMessage) {
        this.slpMessage = slpMessage;
    }

    protected void parseP2PBody(ByteBuffer buffer) {
        slpMessage = new MsnslpRequest();
        slpMessage.readFromBuffer(buffer);
    }

    protected byte[] bodyToMessage() {
        if (slpMessage == null)
            return null;
        return Charset.encodeAsByteArray(slpMessage.toString());
    }
}
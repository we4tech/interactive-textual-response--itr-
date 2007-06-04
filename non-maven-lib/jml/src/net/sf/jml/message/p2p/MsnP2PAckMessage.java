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

/**
 * Acknowledgement of MsnP2PMessage
 * 
 * @author Roger Chen
 */
public class MsnP2PAckMessage extends MsnP2PMessage {

    public MsnP2PAckMessage() {
        setFlag(FLAG_ACK);
    }
    
    public MsnP2PAckMessage(int identifier, String p2pDest ,MsnP2PMessage message) {
        setP2PDest(p2pDest);

        setFlag(FLAG_ACK);
        setIdentifier(identifier);
        setTotalLength(message.getTotalLength());
        setField7(message.getIdentifier());
        setField8(message.getField7());
        setField9(message.getTotalLength());
    }

    protected byte[] bodyToMessage() {
        return null;
    }

    protected void parseP2PBody(ByteBuffer buffer) {
    }
    
}
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

import net.sf.jml.MsnProtocol;
import net.sf.jml.protocol.MsnOutgoingMessage;

/**
 * Send message to other user. See 
 * <a href="http://www.hypothetic.org/docs/msn/switchboard/messages.php">http://www.hypothetic.org/docs/msn/switchboard/messages.php</a> and
 * <a href="http://www.hypothetic.org/docs/msn/research/msnp9.php">http://www.hypothetic.org/docs/msn/research/msnp9.php</a>.
 *
 * Supported Protocol: All
 *
 * Syntax: MSG trId msgType msgLen\r\n content
 * 
 * @author Roger Chen
 */
public class OutgoingMSG extends MsnOutgoingMessage {

    /**
     * Will receive no acknowledgement.
     */
    public static final MsgType TYPE_ACKNOWLEDGE_NONE = new MsgType("U");

    /**
     * Will receive acknowledgement only if the message was not properly received.
     */
    public static final MsgType TYPE_ACKNOWLEDGE_WHEN_ERROR = new MsgType("N");

    /**
     * Will receive acknowledgement whether it was properly received or not.
     */
    public static final MsgType TYPE_ACKNOWLEDGE_ALL = new MsgType("A");

    /**
     * New type when use MSNP11 and send invite message.
     */
    public static final MsgType TYPE_INVITE = new MsgType("S");

    /**
     * It generally behaves like the A type, the official client always uses it 
     * (and only uses it) for special MSNC1 messages.
     */
    public static final MsgType TYPE_MSNC1 = new MsgType("D");

    private static class MsgType {

        private String type;

        private MsgType(String type) {
            this.type = type;
        }

        public String toString() {
            return type;
        }

    }

    public OutgoingMSG(MsnProtocol protocol) {
        super(protocol);
        setCommand("MSG");
        setMsgType(TYPE_ACKNOWLEDGE_ALL);
    }

    protected boolean isSupportChunkData() {
        return true;
    }

    public void setMsgType(MsgType type) {
        if (type != null)
            setParam(0, type.toString());
    }

    public void setMsg(byte[] msg) {
        setChunkData(msg);
    }

}

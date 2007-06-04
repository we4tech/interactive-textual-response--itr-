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
package net.sf.jml.protocol;

import net.sf.jml.MsnProtocol;

/**
 * The incoming MsnMessage.
 * 
 * @author Roger Chen
 */
public abstract class MsnIncomingMessage extends MsnMessage {

    private MsnOutgoingMessage outgoingMessage;

    public MsnIncomingMessage(MsnProtocol protocol) {
        super(protocol);
    }

    public MsnOutgoingMessage getOutgoingMessage() {
        return outgoingMessage;
    }

    void setOutgoingMessage(MsnOutgoingMessage outgoingMessage) {
        this.outgoingMessage = outgoingMessage;
    }

    /**
     * The message have received.
     * 
     * @param session
     * 		the MsnSession which received the message
     */
    protected void messageReceived(MsnSession session) {
    }
}
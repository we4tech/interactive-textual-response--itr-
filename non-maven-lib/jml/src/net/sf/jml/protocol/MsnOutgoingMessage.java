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
 * The outgoing MsnMessage.
 * 
 * @author Roger Chen
 */
public abstract class MsnOutgoingMessage extends MsnMessage {

    public MsnOutgoingMessage(MsnProtocol protocol) {
        super(protocol);
    }

    /**
     * The message have been sent.
     * 
     * @param session
     * 		the MsnSession which sent this message
     */
    protected void messageSent(MsnSession session) {
    }

    /**
     * The message received response. An outgoing message may have more
     * than one response.
     * 
     * @param session
     * 		the MsnSession which received response
     * @param response
     *      received response
     */
    protected void receivedResponse(MsnSession session,
            MsnIncomingMessage response) {
    }

}
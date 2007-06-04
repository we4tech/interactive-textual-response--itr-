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
package net.sf.jml.protocol.incoming;

import net.sf.jml.MsnProtocol;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.outgoing.OutgoingQRY;

/**
 * Msn server check the connection is OK.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: CHL 0 digestNum
 * 
 * @author Roger Chen
 */
public class IncomingCHL extends MsnIncomingMessage {

    public IncomingCHL(MsnProtocol protocol) {
        super(protocol);
    }

    public String getDigestNum() {
        return getParam(0);
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        OutgoingQRY outgoing = new OutgoingQRY(protocol);
        outgoing.setDigestNum(getDigestNum());
        session.sendAsynchronousMessage(outgoing);
    }

}
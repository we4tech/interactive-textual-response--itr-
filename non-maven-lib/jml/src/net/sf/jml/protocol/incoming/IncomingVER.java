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
import net.sf.jml.exception.UnsupportedProtocolException;
import net.sf.jml.impl.BasicMessenger;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.outgoing.OutgoingVER;

/**
 * The response of OutgoingVER, indicate the protocol that 
 * server supported.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: VER trId supportedProtocol1 supportedProtocol2...
 * 
 * @author Roger Chen
 */
public class IncomingVER extends MsnIncomingMessage {

    public IncomingVER(MsnProtocol protocl) {
        super(protocl);
    }

    public MsnProtocol[] getSupportedProtocol() {
        MsnProtocol[] protocols = new MsnProtocol[getParamCount() - 1];
        for (int i = 0; i < protocols.length; i++) {
            protocols[i] = MsnProtocol.parseStr(getParam(i));
        }
        return protocols;
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnProtocol[] supportedProtocol = getSupportedProtocol();
        if (supportedProtocol.length > 0) {
            session.getMessenger().setSupportedProtocol(supportedProtocol);
            ((BasicMessenger) session.getMessenger())
                    .setActualMsnProtocol(supportedProtocol[0]);
        } else {
            throw new UnsupportedProtocolException(
                    ((OutgoingVER) getOutgoingMessage()).getSupportedProtocol());
        }
    }

}
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
 * Exchange supported version.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: VER trId supportedProtocol1 supportedProtocol2...
 * 
 * @author Roger Chen
 */
public class OutgoingVER extends MsnOutgoingMessage {

    public OutgoingVER(MsnProtocol protocol) {
        super(protocol);
        setCommand("VER");
        setSupportedProtocol(null);
    }

    public void setSupportedProtocol(MsnProtocol[] protocol) {
        clearParams();
        if (protocol != null)
            for (int i = 0; i < protocol.length; i++) {
                addParam(protocol[i].toString());
            }
        //maybe CVR0 is the login protocol, official client always include this param
        addParam("CVR0");
    }

    public MsnProtocol[] getSupportedProtocol() {
        MsnProtocol[] protocols = new MsnProtocol[getParamCount() - 1];
        for (int i = 0; i < protocols.length; i++) {
            protocols[i] = MsnProtocol.parseStr(getParam(i));
        }
        return protocols;
    }

}
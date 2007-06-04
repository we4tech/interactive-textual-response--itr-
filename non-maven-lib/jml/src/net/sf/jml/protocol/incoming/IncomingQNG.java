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
import net.sf.jml.util.NumberUtils;

/**
 * The response of OutgoingPNG, indicate the connection is OK.
 * <p>
 * Supported Protocol: All
 * <p>
 * MSNP8 Syntax: QNG
 * <p>
 * MSNP9/MSNP10 Syntax: QNG waitSeconds
 * 
 * @author Roger Chen
 */
public class IncomingQNG extends MsnIncomingMessage {

    public IncomingQNG(MsnProtocol protocol) {
        super(protocol);
    }

    protected boolean isSupportTransactionId() {
        return false;
    }

    public int getWaitSeconds() {
        return NumberUtils.stringToInt(getParam(0), -1);
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        int waitSeconds = getWaitSeconds();
        if (waitSeconds > 0) {
            session.setSessionTimeout(waitSeconds * 1000);
        } else {
            session.setSessionTimeout(60 * 1000); //We can make sure the connection is OK
        }
    }

}
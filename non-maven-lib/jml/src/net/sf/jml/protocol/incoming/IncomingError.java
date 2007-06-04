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

import net.sf.jml.MsnMessageChain;
import net.sf.jml.MsnMessageIterator;
import net.sf.jml.MsnProtocol;
import net.sf.jml.exception.MsnProtocolException;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnOutgoingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;

/**
 * Error message.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax£ºerrorcode trId
 * 
 * @author Roger Chen
 */
public class IncomingError extends MsnIncomingMessage {

    public IncomingError(MsnProtocol protocol) {
        super(protocol);
    }

    public int getErrorCode() {
        return NumberUtils.stringToInt(getCommand());
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        MsnMessageChain chain = session.getOutgoingMessageChain();
        int errorCode = getErrorCode();
        int trId = getTransactionId();

        for (MsnMessageIterator iterator = chain.iterator(); iterator
                .hasPrevious();) {
            MsnOutgoingMessage message = (MsnOutgoingMessage) iterator
                    .previous();
            if (message.getTransactionId() == trId) {
                throw new MsnProtocolException(errorCode, this, message);
            }
        }
    }

}
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
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnOutgoingMessage;
import net.sf.jml.protocol.MsnSession;

/**
 * Check the connection is OK.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: PNG
 * 
 * @author Roger Chen
 */
public class OutgoingPNG extends MsnOutgoingMessage {

    /**
     * Chech that message is for check contact list init completed.
     * If true, when received response indicate that the contact list init completed
     */
    private boolean forCheckContactListInitCompleted = false;

    public OutgoingPNG(MsnProtocol protocol) {
        super(protocol);
        setCommand("PNG");
    }

    protected boolean isSupportTransactionId() {
        return false;
    }

    public boolean isForCheckContactListInitCompleted() {
        return forCheckContactListInitCompleted;
    }

    public void setForCheckContactListInitCompleted(
            boolean forCheckContactListInitCompleted) {
        this.forCheckContactListInitCompleted = forCheckContactListInitCompleted;
    }

    protected void receivedResponse(MsnSession session,
            MsnIncomingMessage response) {
        super.receivedResponse(session, response);
        if (forCheckContactListInitCompleted) {
            ((AbstractMessenger) session.getMessenger())
                    .fireContactListInitCompleted();
        }
    }

}
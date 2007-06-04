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

import net.sf.jml.MsnClientId;
import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnUserStatus;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.MsnOwnerImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.outgoing.OutgoingCHG;
import net.sf.jml.protocol.outgoing.OutgoingPNG;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * OutgoingCHG's response message. User changes status.
 * <p>
 * Supported Protocol: All
 * <p>
 * MSNP8 Syntax£º CHG trId userStatus clientId
 * <p>
 * MSNP9/MSNP10 Syntax£º CHG trId userStatus clientId( msnObject)
 * 
 * @author Roger Chen
 */
public class IncomingCHG extends MsnIncomingMessage {

    public IncomingCHG(MsnProtocol protocol) {
        super(protocol);
    }

    public MsnUserStatus getStatus() {
        return MsnUserStatus.parseStr(getParam(0));
    }

    public int getClientId() {
        return NumberUtils.stringToInt(getParam(1));
    }

    public String getMsnObject() {
        String object = getParam(2);
        if (object != null) {
            return StringUtils.urlDecode(object);
        }
        return null;
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        MsnOwnerImpl owner = (MsnOwnerImpl) session.getMessenger().getOwner();
        owner.fSetClientId(MsnClientId.parseInt(getClientId()));
        owner.fSetStatusF(getStatus());

        ((AbstractMessenger) session.getMessenger()).fireOwnerStatusChanged();

        //The first send CHG, when received response, should send PNG to judge the response is completed
        if ((getOutgoingMessage() instanceof OutgoingCHG)
                && ((OutgoingCHG) getOutgoingMessage()).isFirstSend()) {
            OutgoingPNG message = new OutgoingPNG(protocol);
            message.setForCheckContactListInitCompleted(true);
            session.sendAsynchronousMessage(message);
        }
    }

}
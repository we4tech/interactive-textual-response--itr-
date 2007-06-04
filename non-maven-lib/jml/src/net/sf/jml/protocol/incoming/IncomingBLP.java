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
import net.sf.jml.impl.MsnOwnerImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;

/**
 * The notice of OutgoingSYN or the response of OutgoingBLP,
 * indicate the default list for people who aren't explicitly 
 * allowed or blocked.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax 1: BLP AL|BL
 * <p>
 * Syntax 2: BLP trId versionNum AL|BL
 * <p>
 * Syntax 1 is one of the response of OutgoingSYN, Syntax 2 is 
 * the response of OutgoingBLP.
 * 
 * @author Roger Chen
 */
public class IncomingBLP extends MsnIncomingMessage {

    public IncomingBLP(MsnProtocol protocol) {
        super(protocol);
    }

    public boolean isOnlyNotifyAllowList() {
        return "BL".equals(getParam(getParamCount() - 1));
    }

    public int getVersion() {
        if (getParamCount() > 1) {
            return NumberUtils.stringToInt(getParam(0));
        }
        return -1;
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        MsnOwnerImpl owner = (MsnOwnerImpl) session.getMessenger().getOwner();
        owner.fSetOnlyNotifyAllowList(isOnlyNotifyAllowList());
    }

}
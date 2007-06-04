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
import net.sf.jml.impl.BasicMessenger;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.outgoing.OutgoingXFR;
import net.sf.jml.util.NumberUtils;

/**
 * Call to start a new switchboard or redirect to other NS server.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax 1: XFR trId NS reconnectIP:reconnectPort 0 currentIP:currentPort
 * <p>
 * Syntax 2: XFR trId SB SBIP:SBPort CKI authStr
 * <p>
 * Syntax 1 is the response of OutgoingUSR. For example: 
 * XFR 2 NS 207.46.106.145:1863 0 207.46.104.20:1863, 
 * indicate the NS address.
 * <p>
 * Syntax 2 is thre response of OutgoingXFR. For example:
 * XFR 10 SB 207.46.108.37:1863 CKI 17262740.1050826919.32308,
 * indicate the SB address.
 * 
 * @author Roger Chen
 */
public class IncomingXFR extends MsnIncomingMessage {

    public IncomingXFR(MsnProtocol protocol) {
        super(protocol);
    }

    public boolean isTransferredToSwitchboard() {
        return "SB".equals(getParam(0));
    }

    public String getReconnectHost() {
        String reconnectInfo = getParam(1);
        return reconnectInfo.substring(0, reconnectInfo.indexOf(":"));
    }

    public int getReconnectPort() {
        String reconnectInfo = getParam(1);
        return NumberUtils.stringToInt(reconnectInfo.substring(reconnectInfo
                .indexOf(":") + 1));
    }

    public String getAuthenticationStr() {
        return getParam(3);
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        if (isTransferredToSwitchboard()) {
            Object attachment = ((OutgoingXFR) getOutgoingMessage())
                    .getAttachment();
            ((BasicMessenger) session.getMessenger()).newSwitchboard(
                    getReconnectHost(), getReconnectPort(), true,
                    getAuthenticationStr(), 0, attachment);
        } else {
            session.close();
            ((BasicMessenger) session.getMessenger()).login(getReconnectHost(),
                    getReconnectPort());
        }
    }
}
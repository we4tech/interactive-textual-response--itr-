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

import net.sf.jml.Email;
import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.BasicMessenger;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * Someone invited you to join a SB server.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: RNG sessionId SBIP:SBPort CKI authStr inviterEmail inviterDisplayName
 * 
 * @author Roger Chen
 */
public class IncomingRNG extends MsnIncomingMessage {

    public IncomingRNG(MsnProtocol protocol) {
        super(protocol);
    }

    protected boolean isSupportTransactionId() {
        return false;
    }

    public int getSessionId() {
        return NumberUtils.stringToInt(getParam(0));
    }

    public String getConnectHost() {
        String reconnectInfo = getParam(1);
        return reconnectInfo.substring(0, reconnectInfo.indexOf(":"));
    }

    public int getConnectPort() {
        String reconnectInfo = getParam(1);
        return NumberUtils.stringToInt(reconnectInfo.substring(reconnectInfo
                .indexOf(":") + 1));
    }

    public String getAuthStr() {
        return getParam(3);
    }

    public Email getEmail() {
        return Email.parseStr(getParam(4));
    }

    public String getDisplayName() {
        return StringUtils.urlDecode(getParam(5));
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);
        ((BasicMessenger) session.getMessenger()).newSwitchboard(
                getConnectHost(), getConnectPort(), false, getAuthStr(),
                getSessionId(), null);
    }

}
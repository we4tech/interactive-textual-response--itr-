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
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * The received Message, maybe the notice of MSN Server or 
 * the message sent by other user.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: MSG email displayName msgLen\r\n content
 * 
 * @author Roger Chen
 */
public class IncomingMSG extends MsnIncomingMessage {

    public IncomingMSG(MsnProtocol protocol) {
        super(protocol);
        setCommand("MSG");
    }

    protected boolean isSupportTransactionId() {
        return false;
    }

    protected boolean isSupportChunkData() {
        return true;
    }

    public Email getEmail() {
        return Email.parseStr(getParam(0));
    }

    public String getDisplayName() {
        return StringUtils.urlDecode(getParam(1));
    }

    public int getMsgLen() {
        return NumberUtils.stringToInt(getParam(2));
    }

    public boolean isSystemMsg() {
        return "Hotmail".equals(getParam(0));
    }

    public byte[] getContent() {
        return getChunkData();
    }

}
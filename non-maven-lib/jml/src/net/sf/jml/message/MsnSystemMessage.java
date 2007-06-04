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
package net.sf.jml.message;

import java.nio.ByteBuffer;

import net.sf.jml.MsnConnectionType;
import net.sf.jml.MsnContact;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.MsnConnectionImpl;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.Charset;

/**
 * System message, sent by msn server, such as email notice.
 * 
 * @author Roger Chen
 */
public final class MsnSystemMessage extends MsnMimeMessage {

    private String content;

    MsnSystemMessage() {
    }

    protected void parseBuffer(ByteBuffer buffer) {
        super.parseBuffer(buffer);
        content = Charset.decode(buffer);
    }

    public String getContent() {
        return content;
    }

    protected void messageReceived(MsnSession session, MsnContact contact) {
        super.messageReceived(session, contact);

        //Judge the client is behind NAT device
        String contentType = getContentType();
        if (contentType != null
                && contentType.startsWith(MessageConstants.CT_PROFILE)) {
            MsnConnectionImpl connectin = (MsnConnectionImpl) session
                    .getMessenger().getConnection();
            connectin.setExternalIP(headers.getProperty("ClientIP"));
            connectin.setExternalPort(headers.getIntProperty("ClientPort", -1));

            if (connectin.getInternalIP().equals(connectin.getExternalIP())
                    && connectin.getInternalPort() == connectin
                            .getExternalPort())
                connectin.setConnectionType(MsnConnectionType.DIRECT);
            else
                connectin.setConnectionType(MsnConnectionType.NAT);
            //TODO to check the NAT device supports UPnP 
        }

        ((AbstractMessenger) session.getMessenger())
                .fireSystemMessageReceived(this);
    }
}
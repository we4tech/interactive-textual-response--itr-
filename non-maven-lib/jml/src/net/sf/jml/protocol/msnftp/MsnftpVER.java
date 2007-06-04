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
package net.sf.jml.protocol.msnftp;

import net.sf.jml.MsnProtocol;

/**
 * Exchange version.
 * <p>
 * Syntax: VER MSNFTP
 * <p>
 * Supported Protocol: MSNC0
 * 
 * @author Roger Chen
 */
public class MsnftpVER extends MsnftpMessage {

    public MsnftpVER(MsnProtocol protocol) {
        super(protocol);
        setCommand("VER");
    }

    public void setSupportedFTP(boolean b) {
        setParam(0, b ? "MSNFTP" : "");
    }

    public boolean isSupportedFTP() {
        for (int i = 0; i < getParamCount(); i++) {
            String param = getParam(i);
            if ("MSNFTP".equals(param))
                return true;
        }
        return false;
    }

    protected void messageReceived(MsnftpSession session) {
        super.messageReceived(session);

        if (session.getFileTransfer().isSender()) {
            if (isSupportedFTP()) {
                MsnftpVER message = new MsnftpVER(protocol);
                message.setSupportedFTP(true);
                session.sendAsynchronousMessage(message);
            } else {
                session.close();
            }
        } else {
            MsnftpUSR message = new MsnftpUSR(protocol);
            message.setEmail(session.getFileTransfer().getMessenger()
                    .getOwner().getEmail());
            //            message.setAuthStr(session.getFileTransfer().getAuthCookie());
            session.sendAsynchronousMessage(message);
        }
    }

}
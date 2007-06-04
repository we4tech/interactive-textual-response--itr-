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

import net.sf.jml.Email;
import net.sf.jml.MsnFileTransfer;
import net.sf.jml.MsnProtocol;
import net.sf.jml.util.NumberUtils;

/**
 * Validate current user.
 * <p>
 * Syntax: USR email authStr
 * <p>
 * Supported Protocol: MSNC0
 * 
 * @author Roger Chen
 */
public class MsnftpUSR extends MsnftpMessage {

    public MsnftpUSR(MsnProtocol protocol) {
        super(protocol);
        setCommand("USR");
    }

    public void setEmail(Email email) {
        setParam(0, email.toString());
    }

    public Email getEmail() {
        return Email.parseStr(getParam(0));
    }

    public void setAuthStr(int authStr) {
        setParam(1, String.valueOf(authStr));
    }

    public int getAuthStr() {
        return NumberUtils.stringToInt(getParam(1));
    }

    protected void messageReceived(MsnftpSession session) {
        super.messageReceived(session);

        MsnFileTransfer transfer = session.getFileTransfer();
        if (transfer.isSender()) {
            if (transfer.getContact().getEmail().equals(getEmail())) {
                //TODO                    && transfer.getAuthCookie() == getAuthStr()) {
                MsnftpFIL message = new MsnftpFIL(protocol);
                message.setFileSize(transfer.getFileTotalSize());
                session.sendAsynchronousMessage(message);
            } else {
                session.close();
            }
        } else {
            session.close();
        }
    }

}
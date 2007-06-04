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
import net.sf.jml.util.NumberUtils;

/**
 * File transfer server have ready to transfer file.
 * <p>
 * Syntax: FIL filesize
 * <p>
 * Supported Protocol: MSNC0
 * 
 * @author Roger Chen
 */
public class MsnftpFIL extends MsnftpMessage {

    public MsnftpFIL(MsnProtocol protocol) {
        super(protocol);
        setCommand("FIL");
    }

    public void setFileSize(long fileSize) {
        setParam(0, String.valueOf(fileSize));
    }

    public long getFileSize() {
        return NumberUtils.stringToLong(getParam(0));
    }

    protected void messageReceived(MsnftpSession session) {
        super.messageReceived(session);

        if (session.getFileTransfer().isSender()) {
            session.close();
        } else {
            //TODO            session.getFileTransfer().setFileTotalSize(getFileSize());
            session.sendAsynchronousMessage(new MsnftpTFR(protocol));
        }
    }

}
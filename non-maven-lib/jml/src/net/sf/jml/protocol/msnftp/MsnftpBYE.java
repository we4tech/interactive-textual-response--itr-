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
 * File transfer completed or canceled.
 * <p>
 * Syntax: BYE result
 * <p>
 * Supported Protocol: MSNC0
 * 
 * @author Roger Chen
 */
public class MsnftpBYE extends MsnftpMessage {

    public static final long RESULT_SUCCESS = 16777989L;
    public static final long RESULT_OUT_OF_DISK_SPACE = 2147942405L;
    public static final long RESULT_RECEIVER_CANCELLED_TRANSFER = 2164261682L;
    public static final long RESULT_SENDER_CANCELLED_TRANSFER = 2164261683L;

    public MsnftpBYE(MsnProtocol protocol) {
        super(protocol);
        setCommand("BYE");
    }

    public void setResult(long result) {
        setParam(0, String.valueOf(result));
    }

    public long getResult() {
        return NumberUtils.stringToLong(getParam(0));
    }

    protected void messageReceived(MsnftpSession session) {
        super.messageReceived(session);
        session.close();
    }

    protected void messageSent(MsnftpSession session) {
        super.messageSent(session);
        session.close();
    }

}
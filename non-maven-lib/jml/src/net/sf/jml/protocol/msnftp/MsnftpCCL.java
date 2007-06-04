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
 * Receiver canceled transfer, equals 
 * "BYE RESULT_RECEIVER_CANCELLED_TRANSFER".
 * <p>
 * Syntax: CCL
 * <p>
 * Supported Protocol: MSNC0
 * 
 * @author Roger Chen
 */
public class MsnftpCCL extends MsnftpMessage {

    public MsnftpCCL(MsnProtocol protocol) {
        super(protocol);
        setCommand("CCL");
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
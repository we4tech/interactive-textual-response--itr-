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
import net.sf.jml.protocol.MsnMessage;

/**
 * MSNFTP Message. All MSNFTP Message have no trId, no chunkdata,
 * only have command and params, used for P2P file transfer.
 *
 * Because a MsnMessenger may act as server or client, so all 
 * MSNFTP may be incoming or outgoing.
 *
 * See: <a href="http://www.hypothetic.org/docs/msn/client/file_transfer.php">http://www.hypothetic.org/docs/msn/client/file_transfer.php</a>
 * 
 * @author Roger Chen
 */
public abstract class MsnftpMessage extends MsnMessage {

    public MsnftpMessage(MsnProtocol protocol) {
        super(protocol);
    }

    public final boolean isSupportTransactionId() {
        return false;
    }

    protected void messageReceived(MsnftpSession session) {
    }

    protected void messageSent(MsnftpSession session) {
    }
}

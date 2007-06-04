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

import net.sf.cindy.Message;
import net.sf.cindy.Session;
import net.sf.cindy.SessionAdapter;
import net.sf.cindy.impl.SocketSession;
import net.sf.jml.MsnFileTransfer;
import net.sf.jml.MsnMessenger;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.protocol.WrapperMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MSNFTP session, support write and read MsnftpMessage.
 * 
 * @author Roger Chen
 */
public class MsnftpSession {

    private static final Log log = LogFactory.getLog(MsnftpSession.class);
    private static final int DEFAULT_TIMEOUT = 30000;

    private final MsnFileTransfer transfer;
    private final SocketSession session = new SocketSession();

    public MsnftpSession(MsnFileTransfer transfer) {
        this.transfer = transfer;
        session.setSessionTimeout(DEFAULT_TIMEOUT);
        session.setAttachment(transfer);
        session.setMessageRecognizer(MsnftpMessageRecognizer.getInstance());

        final MsnMessenger messenger = transfer.getMessenger();
        session.addSessionListener(new SessionAdapter() {

            public void sessionTimeout(Session session) {
                session.close();
            }

            public void messageReceived(Session session, Message message) {
                if (messenger.isLogIncoming()) {
                    log.info(messenger.getOwner().getEmail() + " FTP <<< "
                            + message.toString());
                }

                MsnftpMessage ftpMessage = (MsnftpMessage) ((WrapperMessage) message)
                        .getMessage();
                ftpMessage.messageReceived(MsnftpSession.this);
            }

            public void messageSent(Session session, Message message) {
                if (messenger.isLogOutgoing()) {
                    log.info(messenger.getOwner().getEmail() + " FTP >>> "
                            + message.toString());
                }

                MsnftpMessage ftpMessage = (MsnftpMessage) ((WrapperMessage) message)
                        .getMessage();
                ftpMessage.messageSent(MsnftpSession.this);
            }

            public void exceptionCaught(Session session, Throwable cause) {
                ((AbstractMessenger) messenger).fireExceptionCaught(cause);
            }
        });
    }

    public MsnFileTransfer getFileTransfer() {
        return transfer;
    }

    public SocketSession getSocketSession() {
        return session;
    }

    public void close() {
        session.close();
    }

    public void sendAsynchronousMessage(MsnftpMessage message) {
        if (message != null) {
            session.write(new WrapperMessage(message));
        }
    }

    public boolean sendSynchronousMessage(MsnftpMessage message) {
        if (message != null) {
            return session.blockWrite(new WrapperMessage(message));
        }
        return false;
    }
}
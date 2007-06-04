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

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import net.sf.cindy.Message;
import net.sf.cindy.MessageRecognizer;
import net.sf.cindy.Session;
import net.sf.cindy.util.ByteBufferUtils;
import net.sf.jml.MsnFileTransfer;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.protocol.MsnMessage;
import net.sf.jml.protocol.WrapperMessage;
import net.sf.jml.util.Charset;
import net.sf.jml.util.JmlConstants;

/**
 * MSNFTP message recognizer.
 * 
 * @author Roger Chen
 */
final class MsnftpMessageRecognizer implements MessageRecognizer {

    private static final ByteBuffer SPLIT = Charset
            .encode(JmlConstants.LINE_SEPARATOR);

    private static final Map mappingMap = new HashMap();

    static {
        mappingMap.put("VER", MsnftpVER.class);
        mappingMap.put("USR", MsnftpUSR.class);
        mappingMap.put("FIL", MsnftpFIL.class);
        mappingMap.put("CCL", MsnftpCCL.class);
        mappingMap.put("BYE", MsnftpBYE.class);
        mappingMap.put("TFR", MsnftpTFR.class);
    }

    private static MsnftpMessageRecognizer instance = new MsnftpMessageRecognizer();

    public static MsnftpMessageRecognizer getInstance() {
        return instance;
    }

    private MsnftpMessageRecognizer() {
    }

    public Message recognize(Session session, ByteBuffer buffer) {
        if (ByteBufferUtils.indexOf(buffer, SPLIT) < 0)
            return null;
        if (buffer.remaining() < 3)
            return null;

        MsnMessenger messenger = ((MsnFileTransfer) session.getAttachment())
                .getMessenger();
        MsnMessage message = null;

        byte start = buffer.get(buffer.position());
        if (start == 0 || start == 1) { //is msnftpContent
            message = new MsnftpContent(messenger.getActualMsnProtocol());
        } else {
            String key = Charset.decode((ByteBuffer) buffer.limit(buffer
                    .position() + 3));
            Class c = (Class) mappingMap.get(key);
            if (c == null) {
                session.close(false);
                return null;
            } else
                message = getMessageInstance(c, messenger);
        }
        return new WrapperMessage(message);
    }

    private MsnMessage getMessageInstance(Class c, MsnMessenger messenger) {
        try {
            return (MsnMessage) c.getConstructor(
                    new Class[] { MsnProtocol.class }).newInstance(
                    new Object[] { messenger.getActualMsnProtocol() });
        } catch (Exception e) {
            ((AbstractMessenger) messenger).fireExceptionCaught(e);
            return null;
        }
    }
}
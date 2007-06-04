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
package net.sf.jml.protocol;

import java.lang.reflect.Constructor;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import net.sf.cindy.Message;
import net.sf.cindy.MessageRecognizer;
import net.sf.cindy.Session;
import net.sf.cindy.util.ByteBufferUtils;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.message.IncomingMimeMessage;
import net.sf.jml.protocol.incoming.*;
import net.sf.jml.util.Charset;
import net.sf.jml.util.JmlConstants;
import net.sf.jml.util.NumberUtils;

/**
 * Msn Message Recognizer.
 * 
 * @author Roger Chen
 */
final class MsnMessageRecognizer implements MessageRecognizer {

    private static final MsnMessageRecognizer instance = new MsnMessageRecognizer();

    private static final Map normalMappingMap = new HashMap();

    static {
        normalMappingMap.put("MSG", IncomingMimeMessage.class);
        normalMappingMap.put("VER", IncomingVER.class);
        normalMappingMap.put("CVR", IncomingCVR.class);
        normalMappingMap.put("XFR", IncomingXFR.class);
        normalMappingMap.put("USR", IncomingUSR.class);
        normalMappingMap.put("SYN", IncomingSYN.class);
        normalMappingMap.put("GTC", IncomingGTC.class);
        normalMappingMap.put("BLP", IncomingBLP.class);
        normalMappingMap.put("PRP", IncomingPRP.class);
        normalMappingMap.put("SBP", IncomingSBP.class);
        normalMappingMap.put("LSG", IncomingLSG.class);
        normalMappingMap.put("LST", IncomingLST.class);
        normalMappingMap.put("OUT", IncomingOUT.class);
        normalMappingMap.put("CHG", IncomingCHG.class);
        normalMappingMap.put("ILN", IncomingILN.class);
        normalMappingMap.put("FLN", IncomingFLN.class);
        normalMappingMap.put("NLN", IncomingNLN.class);
        normalMappingMap.put("QNG", IncomingQNG.class);
        normalMappingMap.put("CHL", IncomingCHL.class);
        normalMappingMap.put("QRY", IncomingQRY.class);
        normalMappingMap.put("ADD", IncomingADD.class);
        normalMappingMap.put("REM", IncomingREM.class);
        normalMappingMap.put("REA", IncomingREA.class);
        normalMappingMap.put("ADG", IncomingADG.class);
        normalMappingMap.put("RMG", IncomingRMG.class);
        normalMappingMap.put("REG", IncomingREG.class);
        normalMappingMap.put("CAL", IncomingCAL.class);
        normalMappingMap.put("JOI", IncomingJOI.class);
        normalMappingMap.put("BYE", IncomingBYE.class);
        normalMappingMap.put("RNG", IncomingRNG.class);
        normalMappingMap.put("ANS", IncomingANS.class);
        normalMappingMap.put("IRO", IncomingIRO.class);
        normalMappingMap.put("ACK", IncomingACK.class);
        normalMappingMap.put("NAK", IncomingNAK.class);
        normalMappingMap.put("BPR", IncomingBPR.class);
        normalMappingMap.put("ADC", IncomingADC.class);
        normalMappingMap.put("SBS", IncomingSBS.class);
        normalMappingMap.put("URL", IncomingURL.class);
        normalMappingMap.put("UBX", IncomingUBX.class);
        normalMappingMap.put("UUX", IncomingUUX.class);
    }

    private static final ByteBuffer SPLIT = Charset
            .encode(JmlConstants.LINE_SEPARATOR);

    public static MsnMessageRecognizer getInstance() {
        return instance;
    }

    private MsnMessageRecognizer() {
    }

    public Message recognize(Session session, ByteBuffer buffer) {
        if (ByteBufferUtils.indexOf(buffer, SPLIT) < 0)
            return null;
        if (buffer.remaining() < 3)
            return null;
        String charSequence = Charset.decode((ByteBuffer) buffer.limit(buffer
                .position() + 3));

        MsnMessenger messenger = ((MsnSession) session.getAttachment())
                .getMessenger();
        String key = charSequence.substring(0, 3);
        Class c = (Class) normalMappingMap.get(key);

        MsnMessage message = null;
        if (c != null)
            message = getMessageInstance(c, messenger);
        else if (NumberUtils.isDigits(key))
            message = getMessageInstance(IncomingError.class, messenger);
        else
            //don't know how to parse this msg, just skip one line
            message = new IncomingUnknown(messenger.getActualMsnProtocol());

        return new WrapperMessage(message);
    }

    private MsnMessage getMessageInstance(Class c, MsnMessenger messenger) {
        try {
            Constructor constructor = c
                    .getConstructor(new Class[] { MsnProtocol.class });
            return (MsnMessage) constructor
                    .newInstance(new Object[] { messenger
                            .getActualMsnProtocol() });
        } catch (Exception e) {
            ((AbstractMessenger) messenger).fireExceptionCaught(e);
        }
        return null;
    }

}
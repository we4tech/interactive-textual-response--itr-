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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jml.message.invitation.MsnInvitationMessageFactory;
import net.sf.jml.message.p2p.MsnP2PMessageFactory;
import net.sf.jml.util.Charset;
import net.sf.jml.util.JmlConstants;

/**
 * Mime message facotry, judge the message type.
 * 
 * @author Roger Chen
 */
class MsnMimeMessageFactory {

    private static Pattern pattern = Pattern.compile("Content-Type:\\s*(.*)"
            + JmlConstants.LINE_SEPARATOR);

    private static Map map = new LinkedHashMap(); //keep in order

    static {
        map.put(MessageConstants.CT_TEXT, MsnInstantMessage.class);
        map.put(MessageConstants.CT_CONTROL, MsnControlMessage.class);
        map.put(MessageConstants.CT_DATACAST, MsnDatacastMessage.class);
        map.put(MessageConstants.CT_PROFILE, MsnSystemMessage.class);
        map.put(MessageConstants.CT_INIT_MAIL_DATA_NOTIFY,
                MsnSystemMessage.class);
        map.put(MessageConstants.CT_INIT_EMAIL_NOTIFY, MsnSystemMessage.class);
        map.put(MessageConstants.CT_REALTIME_EMAIL_NOTIFY,
                MsnSystemMessage.class);
        map
                .put(MessageConstants.CT_ACTIVE_EMAIL_NOTIFY,
                        MsnSystemMessage.class);
        map.put(MessageConstants.CT_SYSTEM_MESSAGE, MsnSystemMessage.class);
        map.put("", MsnUnknownMessage.class); //default message
    }

    private MsnMimeMessageFactory() {
    }

    public static MsnMimeMessage parseMessage(byte[] message) throws Exception {
        String s = Charset.decode(message);
        String contentType = getContentType(s);
        MsnMimeMessage mimeMessage = parseSpecialMessage(contentType, message,
                s);
        if (mimeMessage == null) {
            mimeMessage = (MsnMimeMessage) getMessageClass(contentType)
                    .newInstance();
        }
        mimeMessage.parseMessage(message);
        return mimeMessage;
    }

    private static String getContentType(String s) {
        Matcher matcher = pattern.matcher(s);
        if (matcher.find())
            return matcher.group(1);
        return "";
    }

    private static MsnMimeMessage parseSpecialMessage(String contentType,
            byte[] message, String s) {
        if (contentType.startsWith(MessageConstants.CT_INVITATION)) {
            return MsnInvitationMessageFactory.parseMessage(s);
        } else if (contentType.startsWith(MessageConstants.CT_P2P))
            return MsnP2PMessageFactory.parseMessage(message);
        return null;
    }

    private static Class getMessageClass(String contentType) {
        Class c = (Class) map.get(contentType);
        if (c == null) {
            for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
                Entry entry = (Entry) iter.next();
                if (contentType.startsWith((String) entry.getKey()))
                    return (Class) entry.getValue();
            }
        }
        return c;
    }

}
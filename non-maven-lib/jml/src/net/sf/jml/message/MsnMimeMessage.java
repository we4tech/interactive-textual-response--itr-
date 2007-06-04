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

import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.cindy.util.ByteBufferUtils;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnProtocol;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.outgoing.OutgoingMSG;
import net.sf.jml.util.Charset;
import net.sf.jml.util.JmlConstants;
import net.sf.jml.util.StringHolder;

/**
 * MIME Message. based on protocol message: OutgoingMSG and IncomingMSG.
 * 
 * @author Roger Chen
 */
public abstract class MsnMimeMessage {

    private static final Pattern charsetPattern = Pattern.compile(
            ".*charset=([\\S&&[^;]]+).*", Pattern.CASE_INSENSITIVE);

    private static final String KEY_MIME_VERSION = "MIME-Version";
    private static final String KEY_CONTENT_TYPE = "Content-Type";

    protected final StringHolder headers = new StringHolder();

    public MsnMimeMessage() {
        setMimeVersion("1.0");
    }

    public final StringHolder getHeaders() {
        return headers;
    }

    public final String getContentType() {
        return headers.getProperty(KEY_CONTENT_TYPE);
    }

    protected void setContentType(String contentType) {
        headers.setProperty(KEY_CONTENT_TYPE, contentType);
    }

    public final String getMimeVersion() {
        return headers.getProperty(KEY_MIME_VERSION);
    }

    protected void setMimeVersion(String mimeVersion) {
        headers.setProperty(KEY_MIME_VERSION, mimeVersion);
    }

    protected final String getCharset() {
        String contentType = getContentType();
        if (contentType != null) {
            Matcher matcher = charsetPattern.matcher(contentType);
            if (matcher.matches()) {
                return matcher.group(1);
            }
        }
        return JmlConstants.DEFAULT_ENCODING;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(headers.toString());
        buffer.append(JmlConstants.LINE_SEPARATOR);
        return buffer.toString();
    }

    /**
     * Generate OutgoingMSGs.
     * 
     * @param protocol
     * 		used MsnProtocol
     * @return
     * 		generated OutgoingMSGs. A MsnMimeMessage may generate multi OutgoingMSGs.
     */
    public OutgoingMSG[] toOutgoingMsg(MsnProtocol protocol) {
        OutgoingMSG message = new OutgoingMSG(protocol);
        message.setMsg(Charset.encodeAsByteArray(toString()));
        message.setMsgType(OutgoingMSG.TYPE_ACKNOWLEDGE_WHEN_ERROR);
        return new OutgoingMSG[] { message };
    }

    private static final ByteBuffer SPLIT = Charset
            .encode(JmlConstants.LINE_SEPARATOR + JmlConstants.LINE_SEPARATOR);

    /**
     * Parse content from IncomingMSG's chunk data.
     * 
     * @param content
     * 		IncomingMSG's chunk data
     */
    protected void parseMessage(byte[] content) {
        int pos = ByteBufferUtils.indexOf(ByteBuffer.wrap(content), SPLIT);
        headers.parseString(Charset.decode(ByteBuffer.wrap(content, 0,
                pos >= 0 ? pos : content.length)));

        int remainingPos = pos + SPLIT.remaining();
        if (pos >= 0 && remainingPos < content.length) {
            ByteBuffer buffer = ByteBuffer.wrap(content, remainingPos,
                    content.length - remainingPos);
            parseBuffer(buffer);
        }
    }

    protected void parseBuffer(ByteBuffer buffer) {
    }

    /**
     * The message have received, invoked by dispatch thread
     * 
     * @param session
     * 		the MsnSession which received the message
     * @param contact
     *      the user who sent this message
     */
    protected void messageReceived(MsnSession session, MsnContact contact) {
    }

}
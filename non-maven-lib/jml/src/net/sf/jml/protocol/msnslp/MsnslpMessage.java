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
package net.sf.jml.protocol.msnslp;

import java.nio.ByteBuffer;

import net.sf.cindy.Message;
import net.sf.cindy.util.ByteBufferUtils;
import net.sf.jml.util.Charset;
import net.sf.jml.util.JmlConstants;
import net.sf.jml.util.StringHolder;

/**
 * MSNSLP message. See <a href="http://zoronax.bot2k3.net/msn6/msnp9/msnslp_p2p.html">http://zoronax.bot2k3.net/msn6/msnp9/msnslp_p2p.html</a>
 * and <a href="http://siebe.bot2k3.net/docs/?url=msnslp.html">http://siebe.bot2k3.net/docs/?url=msnslp.html</a>
 * 
 * @author Roger Chen
 */
public abstract class MsnslpMessage implements Message {

    private static final String KEY_TO = "To";
    private static final String KEY_FROM = "From";
    private static final String KEY_VIA = "Via";
    private static final String KEY_CSEQ = "CSeq";
    private static final String KEY_CALL_ID = "Call-ID";
    private static final String KEY_MAX_FORWARDS = "Max-Forwards";
    private static final String KEY_CONTENT_TYPE = "Content-Type";
    private static final String KEY_CONTENT_LENGTH = "Content-Length";

    private int majorVersion = 1;
    private int minorVersion = 0;

    protected final StringHolder headers = new StringHolder();
    protected final StringHolder bodys = new StringHolder();
    private String body = "";

    public final int getMajorVersion() {
        return majorVersion;
    }

    public final void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public final int getMinorVersion() {
        return minorVersion;
    }

    public final void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public final StringHolder getHeaders() {
        return headers;
    }

    public final StringHolder getBodys() {
        return bodys;
    }


    public final void setTo(String to) {
        headers.setProperty(KEY_TO, to);
    }

    public final String getTo() {
        return headers.getProperty(KEY_TO);
    }

    public final void setFrom(String from) {
        headers.setProperty(KEY_FROM, from);
    }

    public final String getFrom() {
        return headers.getProperty(KEY_FROM);
    }

    public final void setVia(String via) {
        headers.setProperty(KEY_VIA, via);
    }

    public final String getVia() {
        return headers.getProperty(KEY_VIA);
    }

    public final void setCSeq(int cseq) {
        headers.setProperty(KEY_CSEQ, cseq + " ");
    }

    public final int getCSeq() {
        return headers.getIntProperty(KEY_CSEQ);
    }

    public final void setCallId(String callId) {
        headers.setProperty(KEY_CALL_ID, callId);
    }

    public final String getCallId() {
        return headers.getProperty(KEY_CALL_ID);
    }

    public final void setMaxForwards(int maxForwards) {
        headers.setProperty(KEY_MAX_FORWARDS, String.valueOf(maxForwards));
    }

    public final int getMaxForwards() {
        return headers.getIntProperty(KEY_MAX_FORWARDS);
    }

    public final void setContentType(String contentType) {
        headers.setProperty(KEY_CONTENT_TYPE, contentType);
    }

    public final String getContentType() {
        return headers.getProperty(KEY_CONTENT_TYPE);
    }

    private void setContentLength(int contentLength) {
        if (contentLength <= 0) {
            headers.removeProperty(KEY_CONTENT_LENGTH);
        } else {
            headers.setProperty(KEY_CONTENT_LENGTH, String
                    .valueOf(contentLength));
        }
    }

    private int getContentLength() {
        return headers.getIntProperty(KEY_CONTENT_LENGTH);
    }

    public final String getBody() {
        return body;
    }

    public final void setBody(String body) {
        if (body == null) {
            body = "";
        }
        this.body = body;
        this.bodys.parseString(body);
        setContentLength(Charset.encode(body).remaining());
    }

    public final String toString() {
        StringBuffer sb = new StringBuffer();
        writeFirstLine(sb);
        sb.append(JmlConstants.LINE_SEPARATOR);
        sb.append(headers);
        sb.append(JmlConstants.LINE_SEPARATOR);
        sb.append(body);
        return sb.toString();
    }

    public ByteBuffer[] toByteBuffer() {
        return new ByteBuffer[] { Charset.encode(toString()) };
    }

    private static final ByteBuffer SPLIT = Charset
            .encode(JmlConstants.LINE_SEPARATOR + JmlConstants.LINE_SEPARATOR);

    public boolean readFromBuffer(ByteBuffer buffer) {
        int index = ByteBufferUtils.indexOf(buffer, SPLIT);
        if (index < 0)
            return false;

        ByteBuffer sliceBuffer = buffer.slice();
        sliceBuffer.limit(index - buffer.position());
        int headerLen = sliceBuffer.remaining() + SPLIT.remaining();
        String header = Charset.decode(sliceBuffer);

        index = header.indexOf(JmlConstants.LINE_SEPARATOR);
        String firstLine = header.substring(0, index);
        if (readFirstLine(firstLine)) {
            headers.parseString(header.substring(index));

            int contentLength = getContentLength();
            if (buffer.remaining() >= headerLen + contentLength) {
                setBody(Charset.decode((ByteBuffer) (buffer.slice().position(
                        headerLen).limit(headerLen + contentLength))));
                buffer.position(buffer.position() + headerLen + contentLength);
                return true;
            }
        }
        return false;
    }

    protected abstract boolean readFirstLine(String firstLine);

    protected abstract void writeFirstLine(StringBuffer buffer);

}

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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.cindy.util.ByteBufferUtils;
import net.sf.jml.MsnProtocol;
import net.sf.jml.util.Charset;
import net.sf.jml.util.JmlConstants;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * MSN Message. 
 * See: <a href="http://www.hypothetic.org/docs/msn/general/commands.php">http://www.hypothetic.org/docs/msn/general/commands.php</a>
 * 
 * @author Roger Chen
 */
public abstract class MsnMessage {

    private String command;
    private int trId = -1;
    private final List params = new ArrayList();
    private byte[] chunkData;

    protected final MsnProtocol protocol; //the msn protocol which the message used

    protected MsnMessage(MsnProtocol protocol) {
        this.protocol = protocol;
    }

    public final MsnProtocol getProtocol() {
        return protocol;
    }

    public final int getTransactionId() {
        return trId;
    }

    public final String getCommand() {
        return command;
    }

    /**
     * If support transaction id, then can set transaction id.
     * 
     * @return
     * 		is support transaction id
     */
    protected boolean isSupportTransactionId() {
        return true;
    }

    final void setTransactionId(int trId) {
        if (isSupportTransactionId())
            this.trId = trId;
    }

    protected final void setCommand(String command) {
        this.command = command;
    }

    protected final String getParam(int index) {
        if (index < params.size()) {
            return (String) params.get(index);
        }
        return null;
    }

    protected final int getParamCount() {
        return params.size();
    }

    protected final void setParam(int index, String s) {
        for (int i = params.size() - index - 1; i < 0; i++) {
            params.add("");
        }
        params.set(index, s);
    }

    protected final void addParam(String s) {
        if (s != null) {
            params.add(s);
        }
    }

    protected final void clearParams() {
        params.clear();
    }

    /**
     * If support chunk data, then can set chunk data.
     * 
     * @return
     * 		is support chunk data
     */
    protected boolean isSupportChunkData() {
        return false;
    }

    protected final byte[] getChunkData() {
        return chunkData;
    }

    protected final void setChunkData(byte[] chunkData) {
        if (isSupportChunkData())
            this.chunkData = chunkData;
    }

    protected final void setChunkData(String chunkData) {
        this.chunkData = Charset.encodeAsByteArray(chunkData);
    }

    private static final Pattern noChunkPattern = Pattern
            .compile("(\\S{3})(.*)");

    private static final Pattern chunkPattern = Pattern
            .compile("(\\S{3})(.*) (\\d+)");

    private static final ByteBuffer split = Charset
            .encode(JmlConstants.LINE_SEPARATOR);

    protected boolean load(ByteBuffer buffer) {
        int index = ByteBufferUtils.indexOf(buffer, split);
        if (index < 0)
            return false;

        String s = Charset.decode((ByteBuffer) buffer.slice().limit(
                index - buffer.position()));
        Matcher matcher = null;
        if (isSupportChunkData())
            matcher = chunkPattern.matcher(s);
        else
            matcher = noChunkPattern.matcher(s);

        if (!matcher.matches()) //Not match
            return false;
        if (isSupportChunkData()) { //Check chunk data is completed
            int chunkLen = NumberUtils.stringToInt(matcher.group(3));
            if (buffer.limit() < index + split.remaining() + chunkLen)
                return false;
            buffer.position(index + split.remaining());

            chunkData = new byte[chunkLen];
            buffer.get(chunkData);
        } else {
            buffer.position(index + split.remaining());
        }

        setCommand(matcher.group(1));
        String params = matcher.group(2);
        if (params != null) {
            clearParams();

            StringTokenizer st = new StringTokenizer(params);
            if (isSupportTransactionId() && st.hasMoreTokens()) {
                String token = st.nextToken();
                if (NumberUtils.isDigits(token)) {
                    setTransactionId(NumberUtils.stringToInt(token));
                } else {
                    addParam(token);
                }
            }
            while (st.hasMoreTokens()) {
                addParam(st.nextToken());
            }
        }
        return true;
    }

    private String bodyToString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(command);
        if (trId >= 0) {
            buffer.append(" ").append(trId);
        }
        for (Iterator iter = params.iterator(); iter.hasNext();) {
            buffer.append(" ").append(iter.next());
        }
        if (chunkData != null) { //append chunk data len
            buffer.append(" ").append(chunkData.length);
        }
        buffer.append(JmlConstants.LINE_SEPARATOR);
        return buffer.toString();
    }

    protected ByteBuffer[] save() {
        ByteBuffer[] result = new ByteBuffer[chunkData == null ? 1 : 2];
        result[0] = Charset.encode(bodyToString());
        if (chunkData != null)
            result[1] = ByteBuffer.wrap(chunkData);
        return result;
    }

    public String toString() {
        if (chunkData == null)
            return bodyToString();
        StringBuffer buffer = new StringBuffer();
        buffer.append(bodyToString());
        buffer.append("====================").append(
                JmlConstants.LINE_SEPARATOR);
        buffer.append("     Chunk Debug    ").append(
                JmlConstants.LINE_SEPARATOR);
        buffer.append("====================").append(
                JmlConstants.LINE_SEPARATOR);
        buffer.append(Charset.decode(chunkData)).append(
                JmlConstants.LINE_SEPARATOR);
        buffer.append("====================").append(
                JmlConstants.LINE_SEPARATOR);
        buffer.append(" Binary Chunk Debug ").append(
                JmlConstants.LINE_SEPARATOR);
        buffer.append("====================").append(
                JmlConstants.LINE_SEPARATOR);
        buffer.append(StringUtils.debug(ByteBuffer.wrap(chunkData)));
        return buffer.toString();
    }
}

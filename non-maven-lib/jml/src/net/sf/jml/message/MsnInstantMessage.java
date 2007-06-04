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
import java.util.StringTokenizer;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnProtocol;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.outgoing.OutgoingMSG;
import net.sf.jml.util.Charset;
import net.sf.jml.util.GUID;
import net.sf.jml.util.JmlConstants;
import net.sf.jml.util.StringHolder;
import net.sf.jml.util.StringUtils;

/**
 * Normal text message.
 * 
 * @author Roger Chen
 */
public class MsnInstantMessage extends MsnMimeMessage {

    private static final String KEY_FORMAT = "X-MMS-IM-Format";
    private static final String KEY_DISPLAY_NAME = "P4-Context";
    private static final String KEY_MESSAGE_ID = "Message-ID";
    private static final String KEY_CHUNKS = "Chunks";
    private static final String KEY_CHUNK = "Chunk";

    private static final int DEF_SPLIT_SIZE = 1400;

    private String content;

    private String fontName;
    private int fontRGBColor;
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean strikethrough;
    private boolean rightAlign;

    public MsnInstantMessage() {
        setContentType(MessageConstants.CT_TEXT + MessageConstants.CHARSET);
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public int getFontRGBColor() {
        return fontRGBColor;
    }

    public void setFontRGBColor(int fontRGBColor) {
        this.fontRGBColor = fontRGBColor;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public boolean isStrikethrough() {
        return strikethrough;
    }

    public void setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public boolean isRightAlign() {
        return rightAlign;
    }

    public void setRightAlign(boolean rightAlign) {
        this.rightAlign = rightAlign;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDisplayName(String displayName) {
        headers.setProperty(KEY_DISPLAY_NAME, displayName);
    }

    public String getDisplayName() {
        return headers.getProperty(KEY_DISPLAY_NAME);
    }

    private String getEffects() {
        StringBuffer buffer = new StringBuffer();
        if (bold) {
            buffer.append("B");
        }
        if (italic) {
            buffer.append("I");
        }
        if (strikethrough) {
            buffer.append("S");
        }
        if (underline) {
            buffer.append("U");
        }
        return buffer.toString();
    }

    private void setEffects(String effects) {
        setBold(effects.indexOf('B') >= 0);
        setItalic(effects.indexOf('I') >= 0);
        setUnderline(effects.indexOf('U') >= 0);
        setStrikethrough(effects.indexOf('S') >= 0);
    }

    private String getFontColor() {
        int r = (fontRGBColor >> 16) & 0xFF;
        int g = (fontRGBColor >> 8) & 0xFF;
        int b = fontRGBColor & 0xFF;
        int bgr = b << 16 | g << 8 | r;
        return Integer.toHexString(bgr);
    }

    private void setFontColor(String color) {
        int bgr = Integer.parseInt(color, 16);
        int b = (bgr >> 16) & 0xFF;
        int g = (bgr >> 8) & 0xFF;
        int r = bgr & 0xFF;
        setFontRGBColor(r << 16 | g << 8 | b);
    }

    private void setFormat() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("FN=").append(
                StringUtils.urlEncode(fontName == null ? "" : fontName))
                .append(";");
        buffer.append("EF=").append(getEffects()).append(";");
        buffer.append("CO=").append(getFontColor()).append(";");
        buffer.append("RL=").append(rightAlign ? 1 : 0);
        headers.setProperty(KEY_FORMAT, buffer.toString());
    }

    private void getFormat() {
        String format = headers.getProperty(KEY_FORMAT);
        if (format == null) { return; }
        StringTokenizer st = new StringTokenizer(format, ";");
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if (token.startsWith("FN=")) {
                setFontName(StringUtils.urlDecode(token.substring(3)));
            } else if (token.startsWith("EF=")) {
                setEffects(token.substring(3));
            } else if (token.startsWith("CO=")) {
                setFontColor(token.substring(3));
            } else if (token.startsWith("RL=")) {
                setRightAlign(token.substring(3).equals("1"));
            }
        }
    }

    protected void parseBuffer(ByteBuffer buffer) {
        super.parseBuffer(buffer);
        content = net.sf.cindy.util.CharsetUtils.decode(getCharset(), buffer)
                .toString();
    }

    protected void parseMessage(byte[] message) {
        super.parseMessage(message);
        getFormat();
    }

    public OutgoingMSG[] toOutgoingMsg(MsnProtocol protocol) {
        setFormat();

        setContentType(MessageConstants.CT_TEXT + MessageConstants.CHARSET);
        ByteBuffer[] body = Charset.encode(content, DEF_SPLIT_SIZE);
        OutgoingMSG[] outgoing = new OutgoingMSG[body.length];
        String messageId = "{" + GUID.createRandomGuid() + "}";
        for (int i = 0; i < outgoing.length; i++) {
            outgoing[i] = new OutgoingMSG(protocol);
            outgoing[i].setMsgType(OutgoingMSG.TYPE_ACKNOWLEDGE_WHEN_ERROR);

            StringHolder holder = null;
            if (body.length == 1)
                holder = headers;
            else {
                holder = (StringHolder) headers.clone();
                holder.setProperty(KEY_MESSAGE_ID, messageId);
                if (i == 0)
                    holder.setProperty(KEY_CHUNKS, body.length);
                else
                    holder.setProperty(KEY_CHUNK, i);

            }
            ByteBuffer header = Charset.encode(holder.toString()
                    + JmlConstants.LINE_SEPARATOR);

            byte[] b = new byte[body[i].remaining() + header.remaining()];
            ByteBuffer buffer = ByteBuffer.wrap(b);
            buffer.put(header);
            buffer.put(body[i]);
            outgoing[i].setMsg(b);
        }
        return outgoing;
    }

    protected void messageReceived(MsnSession session, MsnContact contact) {
        super.messageReceived(session, contact);

        ((AbstractMessenger) session.getMessenger())
                .fireInstantMessageReceived(session.getSwitchboard(), this,
                        contact);
    }
}

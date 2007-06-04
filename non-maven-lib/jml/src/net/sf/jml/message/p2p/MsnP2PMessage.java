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
package net.sf.jml.message.p2p;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.sf.cindy.util.ByteBufferUtils;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnProtocol;
import net.sf.jml.message.MessageConstants;
import net.sf.jml.message.MsnMimeMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.outgoing.OutgoingMSG;
import net.sf.jml.util.Charset;
import net.sf.jml.util.JmlConstants;

/**
 * Msn P2P message. have a binary header. See:
 * <a href="http://zoronax.bot2k3.net/msn6/msnp9/msnslp_p2p.html">http://zoronax.bot2k3.net/msn6/msnp9/msnslp_p2p.html</a> and
 * <a href="http://siebe.bot2k3.net/docs/?url=binheaders.html">http://siebe.bot2k3.net/docs/?url=binheaders.html</a>.
 * 
 * @author Roger Chen
 */
public abstract class MsnP2PMessage extends MsnMimeMessage {

	protected static final int BINARY_HEADER_LEN = 48;
	protected static final int BINARY_FOOTER_LEN = 4;

	protected static final String KEY_P2P_DEST = "P2P-Dest";

	protected static final int FLAG_NONE = 0x00;
	protected static final int FLAG_ACK = 0x02;
	protected static final int FLAG_BYE_ACK = 0x40;
	protected static final int FLAG_DATA = 0x20;
	protected static final int FLAG_BYE = 0x80;

	private final ByteBuffer binaryHeader = ByteBuffer.allocate(
			BINARY_HEADER_LEN).order(ByteOrder.LITTLE_ENDIAN);

	private final ByteBuffer binaryFooter = ByteBuffer
			.allocate(BINARY_FOOTER_LEN);

	public MsnP2PMessage() {
		setContentType(MessageConstants.CT_P2P);
	}

	public void setP2PDest(String dest) {
		headers.setProperty(KEY_P2P_DEST, dest);
	}

	public String getP2PDest() {
		return headers.getProperty(KEY_P2P_DEST);
	}

	public void setAppId(int appId) {
		binaryFooter.putInt(0, appId);
	}

	public int getAppId() {
		return binaryFooter.getInt(0);
	}

	public void setSessionId(int sessionId) {
		binaryHeader.putInt(0, sessionId);
	}

	public int getSessionId() {
		return binaryHeader.getInt(0);
	}

	public void setIdentifier(int identifier) {
		binaryHeader.putInt(4, identifier);
	}

	public int getIdentifier() {
		return binaryHeader.getInt(4);
	}

	protected void setOffset(long offset) {
		binaryHeader.putLong(8, offset);
	}

	protected long getOffset() {
		return binaryHeader.getLong(8);
	}

	protected void setTotalLength(long totalLength) {
		binaryHeader.putLong(16, totalLength);
	}

	protected long getTotalLength() {
		return binaryHeader.getLong(16);
	}

	protected void setCurrentLength(int currentLength) {
		binaryHeader.putInt(24, currentLength);
	}

	protected int getCurrentLength() {
		return binaryHeader.getInt(24);
	}

	public void setFlag(int flag) {
		binaryHeader.putInt(28, flag);
	}

	protected int getFlag() {
		return binaryHeader.getInt(28);
	}

	public void setField7(int field7) {
		binaryHeader.putInt(32, field7);
	}

	public int getField7() {
		return binaryHeader.getInt(32);
	}

	public void setField8(int field8) {
		binaryHeader.putInt(36, field8);
	}

	public int getField8() {
		return binaryHeader.getInt(36);
	}

	public void setField9(long field9) {
		binaryHeader.putLong(40, field9);
	}

	public long getField9() {
		return binaryHeader.getLong(40);
	}

	protected void parseMessage(byte[] message) {
		ByteBuffer split = Charset.encode(JmlConstants.LINE_SEPARATOR
				+ JmlConstants.LINE_SEPARATOR);
		int pos = ByteBufferUtils.indexOf(ByteBuffer.wrap(message), split);

		// header
		String header = pos == -1 ? Charset.decode(message) : Charset.decode(
				message, 0, pos);
		headers.parseString(header);

		// binaryHeader
		pos += split.remaining();
		binaryHeader.put(message, pos, BINARY_HEADER_LEN);
		binaryHeader.flip();

		// body
		pos += BINARY_HEADER_LEN;
		parseP2PBody(ByteBuffer.wrap(message, pos, message.length - pos
				- BINARY_FOOTER_LEN));

		// binaryFoot
		binaryFooter.put(message, message.length - BINARY_FOOTER_LEN,
				BINARY_FOOTER_LEN);
		binaryFooter.flip();
	}

	public OutgoingMSG[] toOutgoingMsg(MsnProtocol protocol) {
		OutgoingMSG message = new OutgoingMSG(protocol);
		message.setMsgType(OutgoingMSG.TYPE_MSNC1);

		byte[] mimeMessageHeader = Charset.encodeAsByteArray(toString());

		byte[] body = bodyToMessage();
		if (body == null) {
			body = new byte[0];
		}

		ByteBuffer msg = ByteBuffer
				.allocate(mimeMessageHeader.length + this.BINARY_HEADER_LEN
						+ body.length + this.BINARY_FOOTER_LEN);

		msg.put(mimeMessageHeader);
		msg.put(binaryHeader);
		msg.put(body);
		msg.put(binaryFooter);
		message.setMsg(msg.array());
		return new OutgoingMSG[] { message };
	}

	protected abstract void parseP2PBody(ByteBuffer buffer);

	protected abstract byte[] bodyToMessage();

	protected void messageReceived(MsnSession session, MsnContact contact) {
		DisplayPictureDuel duel = DisplayPictureDuelManager.getDuelManager()
				.get(this.getField7());
		if (duel != null) {
			duel.process(this, contact);
		}
	}

}

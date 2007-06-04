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
import net.sf.jml.protocol.msnslp.MsnslpRequest;
import net.sf.jml.util.Charset;
import net.sf.jml.util.JmlConstants;

/**
 * Msn P2P message factory.
 * 
 * @author Roger Chen
 */
public class MsnP2PMessageFactory {

	public static MsnP2PMessage parseMessage(byte[] message) {
		MsnP2PMessage p2pMessage = recognizeMessage(message);
		p2pMessage.parseMessage(message);
		return p2pMessage;
	}

	private static MsnP2PMessage recognizeMessage(byte[] message) {
		ByteBuffer split = Charset.encode(JmlConstants.LINE_SEPARATOR
				+ JmlConstants.LINE_SEPARATOR);
		int pos = ByteBufferUtils.indexOf(ByteBuffer.wrap(message), split);
		// header
		// String header = pos == -1 ? Charset.decode(message) : Charset.decode(
		// message, 0, pos);
		// binaryHeader
		pos += split.remaining();
		ByteBuffer binaryHeader = ByteBuffer.allocate(
				MsnP2PMessage.BINARY_HEADER_LEN).order(ByteOrder.LITTLE_ENDIAN);

		binaryHeader.put(message, pos, MsnP2PMessage.BINARY_HEADER_LEN);
		binaryHeader.flip();

		int sessionId = binaryHeader.getInt(0);
		int flag = binaryHeader.getInt(28);
		int currentLength = binaryHeader.getInt(24);
		long totalLength = binaryHeader.getLong(16);

		// body
		pos += MsnP2PMessage.BINARY_HEADER_LEN;

		// binaryFoot
		ByteBuffer binaryFooter = ByteBuffer
				.allocate(MsnP2PMessage.BINARY_FOOTER_LEN);
		binaryFooter.put(message, message.length
				- MsnP2PMessage.BINARY_FOOTER_LEN,
				MsnP2PMessage.BINARY_FOOTER_LEN);
		binaryFooter.flip();
		int appId = binaryFooter.getInt(0);

		if (flag == MsnP2PMessage.FLAG_ACK) {
			return new MsnP2PAckMessage();
		}

		if (flag == MsnP2PMessage.FLAG_BYE_ACK) {
			return new MsnP2PByeAckMessage();
		}

		if (flag == MsnP2PMessage.FLAG_BYE_ACK) {
			return new MsnP2PByeAckMessage();
		}

		if (flag == MsnP2PMessage.FLAG_DATA) {
			return new MsnP2PDataMessage();
		}

		if (totalLength == 4 && currentLength == 4 && sessionId != 0
				&& appId == 1) {
			return new MsnP2PPreperationMessage();
		}

		if (flag == MsnP2PMessage.FLAG_NONE && sessionId == 0
				&& totalLength == currentLength) {
			/** 为何这么判断？ 因为目前还不太深入了解P2P的数据结构，若不严格，很多情况都会默认为slp,容易产生异常 */
			MsnP2PSlpMessage slpMessage = new MsnP2PSlpMessage();
			slpMessage.parseMessage(message);

			MsnslpRequest msnslpRequest = (MsnslpRequest) slpMessage
					.getSlpMessage();
			if (msnslpRequest.getRequestMethod() != null
					&& msnslpRequest.getRequestMethod().equals(MsnP2PByeMessage.METHOD_BYE)) {
				return new MsnP2PByeMessage();
			}
			if (msnslpRequest.getRequestMethod() != null
					&& msnslpRequest.getRequestMethod()
							.equals(MsnP2PInvitationMessage.METHOD_INVITE)) {
				return new MsnP2PInvitationMessage();
			}

			return slpMessage;
		}

		return new MsnP2PDataMessage();
	}
}
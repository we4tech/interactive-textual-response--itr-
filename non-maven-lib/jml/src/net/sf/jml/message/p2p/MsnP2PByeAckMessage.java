package net.sf.jml.message.p2p;

import java.nio.ByteBuffer;

public class MsnP2PByeAckMessage extends MsnP2PMessage {

	public MsnP2PByeAckMessage() {
		setFlag(FLAG_BYE_ACK);
	}

	public MsnP2PByeAckMessage(int identifier, String p2pDest,
			MsnP2PMessage message) {
		setP2PDest(p2pDest);

		setFlag(FLAG_BYE_ACK);
		setIdentifier(identifier);
		setTotalLength(message.getTotalLength());
		setField7(message.getIdentifier());
		setField8(message.getField7());
		setField9(message.getTotalLength());
	}

	protected byte[] bodyToMessage() {
		return null;
	}

	protected void parseP2PBody(ByteBuffer buffer) {
	}

}

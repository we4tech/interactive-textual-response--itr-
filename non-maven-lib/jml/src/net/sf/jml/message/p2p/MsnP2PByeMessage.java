package net.sf.jml.message.p2p;

import net.sf.jml.MsnContact;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.msnslp.MsnslpRequest;
import net.sf.jml.protocol.outgoing.OutgoingMSG;

public class MsnP2PByeMessage extends MsnP2PSlpMessage {
	
    public static final String METHOD_BYE = "BYE";

	public MsnP2PByeMessage() {
		setFlag(FLAG_BYE);
	}

	public MsnP2PByeMessage(int identifier, String p2pDest,
			MsnP2PMessage message) {
		setP2PDest(p2pDest);

		setFlag(FLAG_BYE);
		setIdentifier(identifier);
		setTotalLength(message.getTotalLength());
		setCurrentLength((int) message.getTotalLength());
		setField7(message.getIdentifier());
	}

	protected void messageReceived(MsnSession session, MsnContact contact) {
			
		MsnslpRequest msnslpRequest = (MsnslpRequest) getSlpMessage();
		if (msnslpRequest.getRequestMethod() != null
				&& msnslpRequest.getRequestMethod().equals(METHOD_BYE)) {
			
				MsnP2PByeAckMessage ack = new MsnP2PByeAckMessage(MsnP2PBaseIdGenerator.getInstance().getNextId(),
						contact.getEmail().getEmailAddress(), this);
				OutgoingMSG[] outgoing = ack.toOutgoingMsg(session
						.getMessenger().getActualMsnProtocol());
				for (int i = 0; i < outgoing.length; i++) {
					session.sendSynchronousMessage(outgoing[i]);
				}
				return;
		}
	}

}

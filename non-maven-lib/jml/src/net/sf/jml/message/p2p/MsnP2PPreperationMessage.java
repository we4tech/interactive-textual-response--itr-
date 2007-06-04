package net.sf.jml.message.p2p;

import java.nio.ByteBuffer;
import net.sf.jml.util.NumberUtils;

public class MsnP2PPreperationMessage  extends MsnP2PMessage {

    private byte[] body;

    public MsnP2PPreperationMessage(){
    }

    public MsnP2PPreperationMessage(int sessionId, int identifier, String p2pDest) {
        setP2PDest(p2pDest);
        setSessionId(sessionId);
        setIdentifier(identifier);
        setTotalLength(4);
        setCurrentLength(4);
        setField7(NumberUtils.getIntRandom());
        setAppId(1);
    }

    protected byte[] bodyToMessage() {
        return new byte[]{0,0,0,0};
    }

    protected void parseP2PBody(ByteBuffer buffer) {
        body = new byte[this.getCurrentLength()];
        buffer.get(body);
    }

}

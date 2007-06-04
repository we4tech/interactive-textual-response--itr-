package net.sf.jml.message.p2p;

import java.nio.ByteBuffer;
import net.sf.jml.util.NumberUtils;

public class MsnP2PDataMessage extends MsnP2PMessage {

    public final static int MAX_DATA_LENGTH = 1202;

    private byte[] body;

    public MsnP2PDataMessage(){
    	setFlag(FLAG_DATA);
    }

    public MsnP2PDataMessage(int sessionId, int identifier, int offset,
                             int totalLength, byte[] data, String p2pDest) {
        this.body = data;
        setP2PDest(p2pDest);
        setSessionId(sessionId);
        setIdentifier(identifier);
        setOffset(offset);
        setTotalLength(totalLength);
        setCurrentLength(body.length);
        setFlag(FLAG_DATA);
        setField7(NumberUtils.getIntRandom());
        setAppId(1);
    }

    protected byte[] bodyToMessage() {
        return body;
    }

    protected void parseP2PBody(ByteBuffer buffer) {
        body = new byte[this.getCurrentLength()];
        buffer.get(body);
    }
}

package net.sf.jml.message.p2p;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnObject;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.msnslp.MsnslpRequest;
import net.sf.jml.util.StringUtils;

public class MsnP2PInvitationMessage extends MsnP2PSlpMessage{
	
	public static final String METHOD_INVITE = "INVITE";

    public static final String KEY_GUID_EUF = "EUF-GUID";
    public static final String KEY_CONTEXT = "Context";

    public static final String GUID_EUF =
            "{A4268EEC-FEC5-49E5-95C3-F126696BDBF6}";
    
    public MsnP2PInvitationMessage() {
		
	}

	protected void messageReceived(MsnSession session, MsnContact contact) {
        MsnslpRequest msnslpRequest = (MsnslpRequest) getSlpMessage();
        if (msnslpRequest.getRequestMethod() != null &&
            msnslpRequest.getRequestMethod().equals(METHOD_INVITE)) {
            if (msnslpRequest.getCSeq() == 0) {
                if (msnslpRequest.getBodys().getProperty(KEY_GUID_EUF) != null &&
                    msnslpRequest.getBodys().getProperty(KEY_GUID_EUF).equals(
                            GUID_EUF)) {
                    String context = StringUtils.decodeBase64(msnslpRequest.getBodys().getProperty(KEY_CONTEXT));
                    if (context != null){
                        context = context.substring(0,context.length()-1);
                        MsnObject picture = DisplayPictureDuelManager.
                                         getDuelManager().getPicture(context);
                        if (picture != null){
                            DisplayPictureDuel duel = new DisplayPictureDuel(
                                    session,picture);
                            DisplayPictureDuelManager.getDuelManager().add(duel);
                            duel.start(this, contact);
                            return;
                        }
                    }
                }
            }
        }
    }

}

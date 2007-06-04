package net.sf.jml.message.invitation;

import net.sf.jml.MsnProtocol;
import net.sf.jml.message.MessageConstants;
import net.sf.jml.message.MsnPropMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.protocol.outgoing.OutgoingMSG;
import net.sf.jml.util.Charset;

/**
 * Invitation message, used for file transfer/video conference and etc.
 * See: <a href="http://www.hypothetic.org/docs/msn/client/invitations.php">http://www.hypothetic.org/docs/msn/client/invitations.php</a>
 * 
 * @author Roger Chen
 */
public abstract class MsnInvitationMessage extends MsnPropMessage {

    protected MsnInvitationMessage() {
        setContentType(MessageConstants.CT_INVITATION
                + MessageConstants.CHARSET);
    }

    public final String getInvitationCommand() {
        return properties
                .getProperty(InvitationConstants.KEY_INVITATION_COMMAND);
    }

    public final int getInvitationCookie() {
        return properties
                .getIntProperty(InvitationConstants.KEY_INVITATION_COOKIE);
    }

    protected final void setInvitationCommand(String invitationCommand) {
        properties.setProperty(InvitationConstants.KEY_INVITATION_COMMAND,
                invitationCommand);
    }

    protected final void setInvitationCookie(int invitationCookie) {
        properties.setProperty(InvitationConstants.KEY_INVITATION_COOKIE,
                invitationCookie);
    }

    public OutgoingMSG[] toOutgoingMsg(MsnProtocol protocol) {
        OutgoingMSG message = new OutgoingMSG(protocol) {

            protected void messageSent(MsnSession session) {
                super.messageSent(session);
                MsnInvitationMessage.this.messageSent(session);
            }
        };
        message.setMsgType(OutgoingMSG.TYPE_ACKNOWLEDGE_WHEN_ERROR);
        message.setMsg(Charset.encodeAsByteArray(toString()));
        return new OutgoingMSG[] { message };
    }

    protected void messageSent(MsnSession session) {
    }

}

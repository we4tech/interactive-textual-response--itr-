package net.sf.jml.message.invitation;

import net.sf.jml.MsnContact;
import net.sf.jml.protocol.MsnSession;

/**
 * Cancel message which invitation command is CANCEL.
 * 
 * @author Roger Chen
 */
public class MsnCancelMessage extends MsnInvitationMessage {

    public static final String FAIL = "FAIL";
    public static final String FTTIMEOUT = "FTTIMEOUT";
    public static final String OUTBANDCANCEL = "OUTBANDCANCEL";
    public static final String REJECT = "REJECT";
    public static final String REJECT_NOT_INSTALLED = "REJECT_NOT_INSTALLED";
    public static final String TIMEOUT = "TIMEOUT";

    private final MsnInviteMessage invite;

    public MsnCancelMessage(MsnInviteMessage invite) {
        this.invite = invite;

        setInvitationCommand(InvitationConstants.COMMAND_CANCEL);
        setInvitationCookie(invite.getInvitationCookie());
    }

    public final String getCancelCode() {
        return properties.getProperty(InvitationConstants.KEY_CANCEL_CODE);
    }

    public final void setCancelCode(String cancelCode) {
        properties.setProperty(InvitationConstants.KEY_CANCEL_CODE, cancelCode);
    }

    public MsnInviteMessage getInviteMessage() {
        return invite;
    }

    protected void messageReceived(MsnSession session, MsnContact contact) {
        super.messageReceived(session, contact);
        InviteCache.uncache(invite);
        invite.cancelled(session);
    }

    protected void messageSent(MsnSession session) {
        super.messageSent(session);
        InviteCache.uncache(invite);
        invite.cancelled(session);
    }
}
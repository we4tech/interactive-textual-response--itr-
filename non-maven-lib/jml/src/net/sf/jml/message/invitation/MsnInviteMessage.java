package net.sf.jml.message.invitation;

import java.util.Random;

import net.sf.jml.MsnContact;
import net.sf.jml.protocol.MsnSession;

/**
 * invite message which invitation command is INVITE.
 * 
 * @author Roger Chen
 */
public abstract class MsnInviteMessage extends MsnInvitationMessage {

    private static final Random ranom = new Random();

    public MsnInviteMessage() {
        setInvitationCommand(InvitationConstants.COMMAND_INVITE);

        int cookie = 0;
        while (true) {
            cookie = ranom.nextInt(Integer.MAX_VALUE);
            if (InviteCache.getInvite(cookie) == null)
                break;
        }
        setInvitationCookie(cookie);
    }

    public final String getApplicationGUID() {
        return properties.getProperty(InvitationConstants.KEY_APP_GUID);
    }

    public final String getApplicationName() {
        return properties.getProperty(InvitationConstants.KEY_APP_NAME);
    }

    protected final void setApplicationGUID(String applicationGUID) {
        properties.setProperty(InvitationConstants.KEY_APP_GUID,
                applicationGUID);
    }

    protected final void setApplicationName(String applicationName) {
        properties.setProperty(InvitationConstants.KEY_APP_NAME,
                applicationName);
    }

    protected void messageReceived(MsnSession session, MsnContact contact) {
        super.messageReceived(session, contact);
        InviteCache.cache(this);
    }

    protected void messageSent(MsnSession session) {
        super.messageSent(session);
        InviteCache.cache(this);
    }

    /**
     * Received or sent MsnCancelMessage.
     * 
     * @param session
     * 		MsnSession
     */
    protected void cancelled(MsnSession session) {
    }

}
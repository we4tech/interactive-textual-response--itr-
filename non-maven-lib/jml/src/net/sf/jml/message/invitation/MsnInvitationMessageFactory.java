package net.sf.jml.message.invitation;

import net.sf.jml.util.StringHolder;

/**
 * Invitation message factory, judge the message type.
 * 
 * @author Roger Chen
 */
public class MsnInvitationMessageFactory {

    public static MsnInvitationMessage parseMessage(String s) {
        StringHolder properties = new StringHolder();
        properties.parseString(s);

        String command = properties
                .getProperty(InvitationConstants.KEY_INVITATION_COMMAND);
        if (command.equalsIgnoreCase(InvitationConstants.COMMAND_INVITE)) {
            return parseInviteMessage(properties);
        } else if (command.equalsIgnoreCase(InvitationConstants.COMMAND_ACCEPT)) {
            return parseAcceptMessage(properties);
        } else if (command.equalsIgnoreCase(InvitationConstants.COMMAND_CANCEL)) {
            return parseCancelMessage(properties);
        }
        return null;
    }

    private static MsnInviteMessage parseInviteMessage(StringHolder properties) {
        String guid = properties.getProperty(InvitationConstants.KEY_APP_GUID);
        if (guid.equals(InvitationConstants.GUID_FILE_TRANSFER)) {
            return new MsnftpInviteMessage();
        }
        return new MsnUnknownInviteMessage();
    }

    private static MsnAcceptMessage parseAcceptMessage(StringHolder properties) {
        int cookie = properties
                .getIntProperty(InvitationConstants.KEY_INVITATION_COOKIE);
        if (cookie > 0) {
            MsnInviteMessage invite = InviteCache.getInvite(cookie);
            if (invite != null) {
                if (invite.getApplicationGUID().equals(
                        InvitationConstants.GUID_FILE_TRANSFER))
                    return new MsnftpAcceptMessage((MsnftpInviteMessage) invite);
                else
                    return new MsnUnknownAcceptMessage(invite);
            }
        }
        return null;
    }

    private static MsnCancelMessage parseCancelMessage(StringHolder properties) {
        int cookie = properties
                .getIntProperty(InvitationConstants.KEY_INVITATION_COOKIE);
        if (cookie > 0) {
            MsnInviteMessage invite = InviteCache.getInvite(cookie);
            if (invite != null) {
                return new MsnCancelMessage(invite);
            }
        }
        return null;
    }

}
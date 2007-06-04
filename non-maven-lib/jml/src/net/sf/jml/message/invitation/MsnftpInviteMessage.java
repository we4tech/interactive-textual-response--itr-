package net.sf.jml.message.invitation;

import java.io.File;

import net.sf.jml.MsnConnectionType;
import net.sf.jml.MsnContact;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;

/**
 * File transfer invite.
 * 
 * @author Roger Chen
 */
public class MsnftpInviteMessage extends MsnInviteMessage {

    private static final String KEY_APP_FILE = "Application-File";
    private static final String KEY_APP_FILESIZE = "Application-FileSize";
    private static final String KEY_CONNECTIVITY = "Connectivity";
    private static final String NAME = "File Transfer";

    private File file;

    public MsnftpInviteMessage() {
        setApplicationName(NAME);
        setApplicationGUID(InvitationConstants.GUID_FILE_TRANSFER);
        setApplicationFileSize(0);
    }

    public String getApplicationFile() {
        return properties.getProperty(KEY_APP_FILE);
    }

    public long getApplicationFileSize() {
        return NumberUtils.stringToLong(properties
                .getProperty(KEY_APP_FILESIZE));
    }

    public void setApplicationFile(String applicationFile) {
        properties.setProperty(KEY_APP_FILE, applicationFile);
    }

    public void setApplicationFileSize(long applicationFileSize) {
        properties.setProperty(KEY_APP_FILESIZE, String
                .valueOf(applicationFileSize));
    }

    public void setConnectionType(MsnConnectionType type) {
        if (type == MsnConnectionType.NAT)
            properties.setProperty(KEY_CONNECTIVITY, "N");
        else if (type == MsnConnectionType.UPNP)
            properties.setProperty(KEY_CONNECTIVITY, "U");
        else
            properties.removeProperty(KEY_CONNECTIVITY);
    }

    public MsnConnectionType getConnectionType() {
        String type = properties.getProperty(KEY_CONNECTIVITY);
        if ("N".equals(type))
            return MsnConnectionType.NAT;
        else if ("U".equals(type))
            return MsnConnectionType.UPNP;
        else
            return MsnConnectionType.DIRECT;
    }

    public void setFile(File file) throws IllegalArgumentException {
        if (file == null || !file.exists())
            throw new IllegalArgumentException("file " + file + " not found");
        this.file = file;
        setApplicationFile(file.getName());
        setApplicationFileSize(file.length());
    }

    protected void messageReceived(MsnSession session, MsnContact contact) {
        super.messageReceived(session, contact);

        //TODO add file transfer support
        MsnCancelMessage message = new MsnCancelMessage(this);
        message.setCancelCode(MsnCancelMessage.REJECT);
        session.getSwitchboard().sendMessage(message);
    }

}
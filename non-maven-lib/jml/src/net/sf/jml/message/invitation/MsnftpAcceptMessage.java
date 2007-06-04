package net.sf.jml.message.invitation;

import net.sf.jml.MsnContact;
import net.sf.jml.protocol.MsnSession;

/**
 * Accept file transfer.
 * 
 * @author Roger Chen
 */
class MsnftpAcceptMessage extends MsnAcceptMessage {

    private static final String KEY_SENDER_CONNECT = "Sender-Connect";
    private static final String KEY_AUTH_COOKIE = "AuthCookie";
    private static final String KEY_IP = "IP-Address";
    private static final String KEY_IP_INTERNAL = "IP-Address-Internal";
    private static final String KEY_PORT = "Port";
    private static final String KEY_PORT_X = "PortX";
    private static final String KEY_PORT_INTERNAL = "PortX-Internal";

    public MsnftpAcceptMessage(MsnftpInviteMessage invite) {
        super(invite);
        properties.setProperty("Request-Data", "IP-Address:");
    }

    public void setIPAddress(String ipAddress) {
        properties.setProperty(KEY_IP, ipAddress);
    }

    public String getIPAddress() {
        return properties.getProperty(KEY_IP);
    }

    public void setIPAddressInternal(String ipAddress) {
        properties.setProperty(KEY_IP_INTERNAL, ipAddress);
    }

    public String getIPAddressInternal() {
        return properties.getProperty(KEY_IP_INTERNAL);
    }

    public void setPort(int port) {
        properties.setProperty(KEY_PORT, port);
    }

    public int getPort() {
        return properties.getIntProperty(KEY_PORT);
    }

    public void setPortX(int portX) {
        properties.setProperty(KEY_PORT_X, portX);
    }

    public int getPortX() {
        return properties.getIntProperty(KEY_PORT_X);
    }

    public void setPortInternal(int port) {
        properties.setProperty(KEY_PORT_INTERNAL, port);
    }

    public int getPortInternal() {
        return properties.getIntProperty(KEY_PORT_INTERNAL);
    }

    public void setSenderConnect(boolean b) {
        properties.setProperty(KEY_SENDER_CONNECT, b ? "TRUE" : "FALSE");
    }

    public boolean isSenderConnect() {
        return "TRUE".equalsIgnoreCase(properties
                .getProperty(KEY_SENDER_CONNECT));
    }

    /**
     * A client connect to a server need to auth itself.
     * 
     * @return
     * 		auth cookie
     */
    public int getAuthCookie() {
        return properties.getIntProperty(KEY_AUTH_COOKIE);
    }

    void initAuthCookie() {
        properties.setProperty(KEY_AUTH_COOKIE,
                (int) (Math.random() * Integer.MAX_VALUE));
    }

    protected void messageReceived(MsnSession session, MsnContact contact) {
        super.messageReceived(session, contact);

        //TODO add file transfer support
    }

}
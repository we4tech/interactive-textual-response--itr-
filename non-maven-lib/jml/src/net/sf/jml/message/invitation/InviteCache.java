package net.sf.jml.message.invitation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Cache all invite message until the invite finished or canceled.
 * 
 * @author Roger Chen
 */
final class InviteCache {

    private InviteCache() {
    }

    private static Map cache = Collections.synchronizedMap(new HashMap());

    public static void cache(MsnInviteMessage invite) {
        cache.put(new Integer(invite.getInvitationCookie()), invite);
    }

    public static MsnInviteMessage getInvite(int invitationCookie) {
        return (MsnInviteMessage) cache.get(new Integer(invitationCookie));
    }

    public static void uncache(MsnInviteMessage invite) {
        cache.remove(new Integer(invite.getInvitationCookie()));
    }

}
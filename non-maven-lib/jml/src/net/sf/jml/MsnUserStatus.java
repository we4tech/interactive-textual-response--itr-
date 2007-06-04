/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jml;

import java.util.HashMap;
import java.util.Map;

/**
 * MsnUser status, See
 * <a href="http://www.hypothetic.org/docs/msn/notification/presence.php">http://www.hypothetic.org/docs/msn/notification/presence.php</a>
 * "Statuses" Section.
 * 
 * @author Roger Chen
 */
public final class MsnUserStatus {

    /**
     * Online.
     */
    public static final MsnUserStatus ONLINE = new MsnUserStatus("NLN",
            "ONLINE");

    /**
     * Busy.
     */
    public static final MsnUserStatus BUSY = new MsnUserStatus("BSY", "BUSY");

    /**
     * Idle.
     */
    public static final MsnUserStatus IDLE = new MsnUserStatus("IDL", "IDLE");

    /**
     * Be right back.
     */
    public static final MsnUserStatus BE_RIGHT_BACK = new MsnUserStatus("BRB",
            "BE RIGHT BACK");

    /**
     * Away.
     */
    public static final MsnUserStatus AWAY = new MsnUserStatus("AWY", "AWAY");

    /**
     * On the phone.
     */
    public static final MsnUserStatus ON_THE_PHONE = new MsnUserStatus("PHN",
            "ON THE PHONE");

    /**
     * Out to lunch.
     */
    public static final MsnUserStatus OUT_TO_LUNCH = new MsnUserStatus("LUN",
            "OUT TO LUNCH");

    /**
     * Hide.
     */
    public static final MsnUserStatus HIDE = new MsnUserStatus("HDN", "HIDE");

    /**
     * Offline. Can't set owner's status to offline.
     */
    public static final MsnUserStatus OFFLINE = new MsnUserStatus("FLN",
            "OFFLINE");

    private static Map statusMap = new HashMap();

    static {
        statusMap.put(ONLINE.status.toUpperCase(), ONLINE);
        statusMap.put(BUSY.status.toUpperCase(), BUSY);
        statusMap.put(IDLE.status.toUpperCase(), IDLE);
        statusMap.put(BE_RIGHT_BACK.status.toUpperCase(), BE_RIGHT_BACK);
        statusMap.put(AWAY.status.toUpperCase(), AWAY);
        statusMap.put(ON_THE_PHONE.status.toUpperCase(), ON_THE_PHONE);
        statusMap.put(OUT_TO_LUNCH.status.toUpperCase(), OUT_TO_LUNCH);
        statusMap.put(HIDE.status.toUpperCase(), HIDE);
    }

    public static MsnUserStatus parseStr(String s) {
        return s == null ? null : (MsnUserStatus) statusMap
                .get(s.toUpperCase());
    }

    private String status;
    private String displayStatus;

    private MsnUserStatus(String status, String displayStatus) {
        this.status = status;
        this.displayStatus = displayStatus;
    }

    /**
     * Get the status used for msn protocol.
     * 
     * @return
     * 		the status used for msn protocol
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get the status used for display
     * 
     * @return
     * 		the status used for display
     */
    public String getDisplayStatus() {
        return displayStatus;
    }

    public String toString() {
        return displayStatus;
    }
}

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
 * Properties type, See
 * <a href="http://www.hypothetic.org/docs/msn/notification/get_details.php">http://www.hypothetic.org/docs/msn/notification/get_details.php</a>
 * "Phone Numbers" section.
 * 
 * @author Roger Chen
 */
public final class MsnUserPropertyType {

    /**
     * Home phone number.
     */
    public static final MsnUserPropertyType PHH = new MsnUserPropertyType(
            "PHH", "home phone number");

    /**
     * Work phone number.
     */
    public static final MsnUserPropertyType PHW = new MsnUserPropertyType(
            "PHW", "work phone number");

    /**
     * Mobile phone number.
     */
    public static final MsnUserPropertyType PHM = new MsnUserPropertyType(
            "PHM", "mobile phone number");

    /**
     * Allow be contacted on MSN mobile device. If have, then the property is "Y".
     */
    public static final MsnUserPropertyType MOB = new MsnUserPropertyType(
            "MOB",
            "are other people authorised to contact me on my MSN Mobile device");

    /**
     * Has device enabled on MSN mobile. If have, then the property is "Y".
     */
    public static final MsnUserPropertyType MBE = new MsnUserPropertyType(
            "MBE", "has a mobile device enabled on MSN Mobile");

    /**
     * Has device enabled on MSN direct. If have, then the property is "2".
     */
    public static final MsnUserPropertyType WWE = new MsnUserPropertyType(
            "WWE", "has a device enabled on MSN Direct");

    /**
     * Display name, MSNP10/MSNP11 use this to indicate user's display name.
     */
    public static final MsnUserPropertyType MFN = new MsnUserPropertyType(
            "MFN", "display name");

    /**
     * Has space on MSN spaces.
     */
    public static final MsnUserPropertyType HSB = new MsnUserPropertyType(
            "HSB", "has space on MSN spaces");

    private static Map typeMap = new HashMap();

    static {
        typeMap.put(PHH.getType().toUpperCase(), PHH);
        typeMap.put(PHW.getType().toUpperCase(), PHW);
        typeMap.put(PHM.getType().toUpperCase(), PHM);
        typeMap.put(MOB.getType().toUpperCase(), MOB);
        typeMap.put(MBE.getType().toUpperCase(), MBE);
        typeMap.put(WWE.getType().toUpperCase(), WWE);
        typeMap.put(MFN.getType().toUpperCase(), MFN);
        typeMap.put(HSB.getType().toUpperCase(), HSB);
    }

    public static MsnUserPropertyType parseStr(String s) {
        return s == null ? null : (MsnUserPropertyType) typeMap.get(s
                .toUpperCase());
    }

    private String type;
    private String displayType;

    private MsnUserPropertyType(String type, String displayType) {
        this.type = type;
        this.displayType = displayType;
    }

    /**
     * Get the type used for msn protocol.
     * 
     * @return
     * 		the type used for msn protocol
     */
    public String getType() {
        return type;
    }

    /**
     * Get the type for display.
     * 
     * @return
     * 		the type for display
     */
    public String getDisplayType() {
        return displayType;
    }

    public String toString() {
        return type;
    }
}

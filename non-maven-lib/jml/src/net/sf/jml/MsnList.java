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
 * Msn List, currently only have five types: FL/AL/BL/RL/PL.
 * See: <a href="http://www.hypothetic.org/docs/msn/notification/get_details.php">http://www.hypothetic.org/docs/msn/notification/get_details.php</a>
 * "Background information" Section.
 * 
 * @author Roger Chen
 */
public class MsnList {

    /**
     * Forward List.
     */
    public static final MsnList FL = new MsnList(1, "FL", "Forward List");

    /**
     * Allow List.
     */
    public static final MsnList AL = new MsnList(1 << 1, "AL", "Allow List");

    /**
     * Block List.
     */
    public static final MsnList BL = new MsnList(1 << 2, "BL", "Block List");

    /**
     * Reverse List.
     */
    public static final MsnList RL = new MsnList(1 << 3, "RL", "Reverse List");

    /**
     * Pending List
     */
    public static final MsnList PL = new MsnList(1 << 4, "PL", "Pending List");

    private static Map listMap = new HashMap();

    static {
        listMap.put(FL.getListName().toUpperCase(), FL);
        listMap.put(AL.getListName().toUpperCase(), AL);
        listMap.put(BL.getListName().toUpperCase(), BL);
        listMap.put(RL.getListName().toUpperCase(), RL);
        listMap.put(PL.getListName().toUpperCase(), PL);
    }

    public static MsnList parseStr(String s) {
        return s == null ? null : (MsnList) listMap.get(s.toUpperCase());
    }

    private int listId;
    private String listName;
    private String listDescirption;

    private MsnList() {
    }

    private MsnList(int listId, String listName, String listDescription) {
        this.listId = listId;
        this.listName = listName;
        this.listDescirption = listDescription;
    }

    public int getListId() {
        return listId;
    }

    public String getListName() {
        return listName;
    }

    public String getListDescirption() {
        return listDescirption;
    }

    public String toString() {
        return listName + "<" + listDescirption + ">";
    }
}

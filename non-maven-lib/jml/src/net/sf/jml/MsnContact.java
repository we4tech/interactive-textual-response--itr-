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

/**
 * Msn Contact. See
 * <a href="http://www.hypothetic.org/docs/msn/notification/get_details.php">http://www.hypothetic.org/docs/msn/notification/get_details.php</a>
 * "Background information" section.
 * 
 * @author Roger Chen
 */
public interface MsnContact extends MsnUser {

    /**
     * Get the contact list the contact belongs to.
     * 
     * @return
     * 		contact list 
     */
    MsnContactList getContactList();

    /**
     * MSNP10/MSNP11 protocol use a GUID to differentiate 
     * user, not use user's email. If user not in 
     * FL, will use email instead. 
     * <p>
     * When login with MSNP8/MSNP9, will use email instead.
     * 
     * @return
     * 		id
     */
    public String getId();

    /**
     * The name that MsnOwner assigned to the user. If user not
     * int FL, will use email instead.
     * 
     * @return
     * 		friendly name
     */
    public String getFriendlyName();

    /**
     * Judge is in the MsnList.
     * 
     * @param list
     * 		MsnList
     * @return	
     * 		is in the list
     */
    public boolean isInList(MsnList list);

    /**
     * Get the groups which the user belongs.
     * 
     * @return
     * 		the groups which the user belongs to 
     */
    public MsnGroup[] getBelongGroups();

    /**
     * Is belong the group.
     * 
     * @param group
     *      MsnGroup
     * @return
     *      is belong the group
     */
    public boolean belongGroup(MsnGroup group);

}

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
 * Contact list. See
 * <a href="http://www.hypothetic.org/docs/msn/notification/get_details.php">http://www.hypothetic.org/docs/msn/notification/get_details.php</a>
 * "Background information" section.
 * 
 * @author Roger Chen
 */
public interface MsnContactList {

    /**
     * Get the MsnMessenger the contact list belongs to.
     * 
     * @return	
     * 		MsnMessenger
     */
    public MsnMessenger getMessenger();

    /**
     * Contact list version number. If the version is the same
     * as msn server contact list version, OutgoingSYN will 
     * not return the whole contact list.
     *
     * @return
     * 		contact list version
     */
    public String getVersion();

    /**
     * Get the groups in the contact list.
     * 
     * @return
     * 		groups in the contact list
     */
    public MsnGroup[] getGroups();

    /**
     * Get the group in the contact list.
     * 
     * @param groupId
     * 		group id
     * @return
     * 		MsnGroup 
     */
    public MsnGroup getGroup(String groupId);

    /**
     * Get the default group.
     * 
     * @return
     * 		default group
     */
    public MsnGroup getDefaultGroup();

    /**
     * Get the contacts in the contact list.
     * 
     * @return
     * 		the contacts in the contact list 
     */
    public MsnContact[] getContacts();

    /**
     * Get the contact in the contact list.
     * 
     * @param id
     * 		contact's id
     * @return
     * 		MsnContact
     */
    public MsnContact getContactById(String id);

    /**
     * Get the contact in the contact list.
     * 
     * @param email
     * 		contact's email
     * @return
     * 		MsnContact
     */
    public MsnContact getContactByEmail(Email email);

    /**
     * Get the contacts in the MsnList.
     * 
     * @param list
     * 		MsnList
     * @return
     * 		the contacts in the MsnList
     */
    public MsnContact[] getContactsInList(MsnList list);

}

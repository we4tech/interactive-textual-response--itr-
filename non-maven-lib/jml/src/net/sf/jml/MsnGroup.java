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
 * Msn Group, See
 * <a href="http://www.hypothetic.org/docs/msn/notification/get_details.php">http://www.hypothetic.org/docs/msn/notification/get_details.php</a>
 * "Background information" section.
 * 
 * @author Roger Chen
 */
public interface MsnGroup {

    /**
     * Get the contact list the group belongs to.
     * 
     * @return
     * 		contact list 
     */
    MsnContactList getContactList();

    /**
     * Get group id which can't be changed.
     * 
     * @return
     * 		group id
     */
    public String getGroupId();

    /**
     * Get group name which can be changed.
     * 
     * @return
     * 		group name
     */
    public String getGroupName();

    /**
     * Get the contacts belong the group.
     * 
     * @return
     * 		the contacts belong the group
     */
    public MsnContact[] getContacts();

    /**
     * Contain contact.
     * 
     * @param contact
     *      MsnContact
     * @return
     *      is contain contact
     */
    public boolean containContact(MsnContact contact);

    /**
     * Is default group.
     * 
     * @return
     * 		is default group
     */
    public boolean isDefaultGroup();
}

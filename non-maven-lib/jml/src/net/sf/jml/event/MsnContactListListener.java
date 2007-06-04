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
package net.sf.jml.event;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnGroup;

/**
 * Contact list listener.
 * 
 * @author Roger Chen
 */
public interface MsnContactListListener {

    /**
     * Contact list synchronize completed. Now all friends in
     * contact list, but their status have not been determined. 
     * 
     * @param messenger
     * 		MsnMessenger
     */
    public void contactListSyncCompleted(MsnMessenger messenger);

    /**
     * Contact list init completed. Now all user status 
     * have been determined.
     * 
     * @param messenger
     *    	MsnMessenger
     */
    public void contactListInitCompleted(MsnMessenger messenger);

    /**
     * Contact status changed such as online and offline or friend
     * changed his display name.
     * 
     * @param messenger
     *      MsnMessenger
     * @param contact
     *   	contact
     */
    public void contactStatusChanged(MsnMessenger messenger, MsnContact contact);

    /**
     * Owner status changed or name changed or profile changed.
     * 
     * @param messenger
     *		MsnMessenger
     */
    public void ownerStatusChanged(MsnMessenger messenger);

    /**
     * Some one add current login user to his contact list.
     * 
     * @param messenger
     * 		MsnMessenger
     * @param contact
     *      the one who add you
     */
    public void contactAddedMe(MsnMessenger messenger, MsnContact contact);

    /**
     * Some one remove current login user from his contact list.
     * 
     * @param messenger
     * 		MsnMessenger
     * @param contact
     *      the one who remove you
     */
    public void contactRemovedMe(MsnMessenger messenger, MsnContact contact);

    /**
     * A contact you requested to be added has been added to the server.
     *
     * @param messenger
     *      MsnMessenger
     * @param contact
     *      the contact that you added
     */
    public void contactAddCompleted(MsnMessenger messenger, MsnContact contact);

    /**
     * A contact you requested to be removed has been removed from the server.
     *
     * @param messenger
     *      MsnMessenger
     * @param contact
     *      the contact that you removed
     */
    public void contactRemoveCompleted(MsnMessenger messenger, MsnContact contact);

    /**
     * A group you requested to be added has been added to the server.
     *
     * @param messenger
     *      MsnMessenger
     * @param group
     *      the group that you added
     */
    public void groupAddCompleted(MsnMessenger messenger, MsnGroup group);

    /**
     * A group you requested to be removed has been removed from the server.
     *
     * @param messenger
     *      MsnMessenger
     * @param group
     *      the group that you removed
     */
    public void groupRemoveCompleted(MsnMessenger messenger, MsnGroup group);

}
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

import net.sf.jml.util.GUID;

/**
 * Current login user.
 * 
 * @author Roger Chen
 */
public interface MsnOwner extends MsnUser {

    /**
     * Get the MsnMessenger the owner belongs to.
     * 
     * @return	
     * 		MsnMessenger
     */
    public MsnMessenger getMessenger();

    /**
     * The email have been verified.
     * 
     * @return
     * 		the email have been verified
     */
    public boolean isVerified();

    /**
     * Get the init status. When login to NS, will use this status to init.
     * 
     * @return
     * 		init status
     */
    public MsnUserStatus getInitStatus();

    /**
     * Set the init status. 
     * 
     * @param status
     * 		init status
     */
    public void setInitStatus(MsnUserStatus status);

    /**
     * Set display name.
     * 
     * @param displayName
     * 		display name
     */
    public void setDisplayName(String displayName);

    /**
     * Set client id.
     * 
     * @param clientId
     * 		client id
     */
    public void setClientId(MsnClientId clientId);

    /**
     * Set status.
     * 
     * @param status
     * 		status
     */
    public void setStatus(MsnUserStatus status);

    /**
     * Set user property. See 
     * <a href="http://www.hypothetic.org/docs/msn/notification/get_details.php">http://www.hypothetic.org/docs/msn/notification/get_details.php</a>
     * "Phone Numbers" section.
     * 
     * @param type
     * 		property type
     * @param value
     * 		property value
     */
    public void setProperty(MsnUserPropertyType type, String value);

    /**
     * If result is true, the contacts neither on AL or BL will be blocked, otherwise
     * only the contacts on AL will received my notify message.
     *
     * See <a href="http://www.hypothetic.org/docs/msn/notification/get_details.php">http://www.hypothetic.org/docs/msn/notification/get_details.php</a>
     * "Privacy Settings -- BLP" section.
     * 
     *  @return
     * 		is only notify AL
     */
    public boolean isOnlyNotifyAllowList();

    /**
     * if set to true, the contacts neither on AL or BL will be blocked.
     *
     * See <a href="http://www.hypothetic.org/docs/msn/notification/get_details.php">http://www.hypothetic.org/docs/msn/notification/get_details.php</a>
     * "Privacy Settings -- BLP" section.
     * 
     * @param b
     * 		is only notify AL
     */
    public void setOnlyNotifyAllowList(boolean b);

    /**
     * See <a href="http://www.hypothetic.org/docs/msn/notification/get_details.php">http://www.hypothetic.org/docs/msn/notification/get_details.php</a>
     * "Privacy Settings -- GTC" section.
     * 
     * @return
     * 		is notify me when someone added me
     */
    public boolean isNotifyMeWhenSomeoneAddedMe();

    /**
     * See <a href="http://www.hypothetic.org/docs/msn/notification/get_details.php">http://www.hypothetic.org/docs/msn/notification/get_details.php</a>
     * "Privacy Settings -- GTC" section.
     * 
     * @param b
     * 		is notify me when someone added me
     */
    public void setNotifyMeWhenSomeoneAddedMe(boolean b);
    
    public void setDisplayPicture(MsnObject picture);

    public void setInitDisplayPicture(MsnObject initDisplayPictures);

    public MsnObject getDisplayPicture();

    public void setPersonalMessage(String info);

    public void setCurrentMedia(String info);
    
    public void setCurrentMedia(String title, String artist, String album, GUID contentID);

}

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

import net.sf.jml.event.MsnAdapter;
import net.sf.jml.event.MsnContactListListener;
import net.sf.jml.event.MsnFileTransferListener;
import net.sf.jml.event.MsnMessageListener;
import net.sf.jml.event.MsnMessengerListener;
import net.sf.jml.event.MsnSwitchboardListener;
import net.sf.jml.protocol.MsnOutgoingMessage;

/**
 * Msn Messenger interface.
 * 
 * @author Roger Chen
 */
public interface MsnMessenger {

    /**
     * Get the attachment.
     * 
     * @return
     * 		attachment
     */
    public Object getAttachment();

    /**
     * Set the attachment.
     * 
     * @param attachment
     * 		attachment
     */
    public void setAttachment(Object attachment);

    /**
     * Is log incoming message. For debug purpose.
     * 
     * @return
     * 		is log incoming message
     */
    public boolean isLogIncoming();

    /**
     * Set log incoming message. For debug purpose.
     * 
     * @param logIncoming
     * 		set log incoming message
     */
    public void setLogIncoming(boolean logIncoming);

    /**
     * Is log outgoing message. For debug purpose.
     * 
     * @return
     * 		is log outgoing message
     */
    public boolean isLogOutgoing();

    /**
     * Set log outgoing message. For debug purpose.
     * 
     * @param logOutgoing
     * 		set log outgoing message
     */
    public void setLogOutgoing(boolean logOutgoing);

    /**
     * Get supported protocols.
     * 
     * @return
     * 		supported protocols
     */
    public MsnProtocol[] getSupportedProtocol();

    /**
     * Set supported protocols. This will take effect only after re-login.
     * 
     * @param supportedProtocol
     * 		supported protocols
     */
    public void setSupportedProtocol(MsnProtocol[] supportedProtocol);

    /**
     * Get current user.
     * 
     * @return
     * 		current user
     */
    public MsnOwner getOwner();

    /**
     * Get contact list. 
     * 
     * @return
     * 		contact list
     */
    public MsnContactList getContactList();

    /**
     * Get current connection information.
     * 
     * @return
     * 		current connect information
     */
    public MsnConnection getConnection();

    /**
     * Get actual used protocol.
     * 
     * @return
     * 		current used protocol
     */
    public MsnProtocol getActualMsnProtocol();

    /**
     * Get the outgoing message chain.
     * 
     * @return
     * 		outgoing message chain
     */
    public MsnMessageChain getOutgoingMessageChain();

    /**
     * Get the incoming message chain.
     * 
     * @return
     * 		incoming message chain
     */
    public MsnMessageChain getIncomingMessageChain();

    /**
     * Login.
     */
    public void login();

    /**
     * Logout.
     */
    public void logout();

    /**
     * Send a message to DS/NS server. If block, the method will return
     * after the message successfully sent or failed. If not block, the
     * method always return false.
     * 
     * @param message
     * 		MsnOutgoingMessage  
     * @param block
     * 		is block
     * @return
     * 		if block, return message send successful, else return false  
     */
    public boolean send(MsnOutgoingMessage message, boolean block);

    /**
     * This method is a shorthand for:
     * <pre>
     *     send(message, false);
     * </pre>
     * 
     * @param message
     * 		MsnOutgoingMessage
     */
    public void send(MsnOutgoingMessage message);

    /**
     * Create a switchboard and start. Send a message to NS server
     * and wait response to start a new switchboard. 
     * <p>
     * You can use the attachement to identify the MsnSwitchboard by
     * call switchboard.getAttachment().
     * 
     * @param attachment
     * 		MsnSwitchboard's attachment
     */
    public void newSwitchboard(Object attachment);

    /**
     * Get all active MsnSwitchboard.
     * 
     * @return
     * 		all active MsnSwitchboard
     */
    public MsnSwitchboard[] getActiveSwitchboards();

    public void addListener(MsnAdapter listener);

    public void addMessengerListener(MsnMessengerListener listener);

    public void removeMessengerListener(MsnMessengerListener listener);

    public void addMessageListener(MsnMessageListener listener);

    public void removeMessageListener(MsnMessageListener listener);

    public void addContactListListener(MsnContactListListener listener);

    public void removeContactListListener(MsnContactListListener listener);

    public void addSwitchboardListener(MsnSwitchboardListener listener);

    public void removeSwitchboardListener(MsnSwitchboardListener listener);

    public void addFileTransferListener(MsnFileTransferListener listener);

    public void removeFileTransferListener(MsnFileTransferListener listener);

    /**
     * Send text message to someone without format. If the email address is not
     * in any switchboard, will create a switchboard and send the text. 
     * 
     * @param email
     * 		email
     * @param text
     * 		text
     */
    public void sendText(Email email, String text);

    /**
     * Add group.
     * 
     * @param groupName
     * 		group name
     */
    public void addGroup(String groupName);

    /**
     * Remove group.
     * 
     * @param groupId
     * 		group id
     */
    public void removeGroup(String groupId);

    /**
     * Rename group.
     * 
     * @param groupId
     * 		group id
     * @param newGroupName
     * 		new group name
     */
    public void renameGroup(String groupId, String newGroupName);

    /**
     * Add friend to FL and AL.
     * 
     * @param email
     *  	email
     * @param friendlyName
     * 		friendly name
     */
    public void addFriend(Email email, String friendlyName);

    /**
     * Copy friend to other group, but user can't both in 
     * default group and user defined group. 
     * 
     * @param email
     * 		email
     * @param groupId
     *      group id 
     */
    public void copyFriend(Email email, String groupId);

    /**
     * Remove friend.
     * 
     * @param email
     * 		email
     * @param block
     * 		remove and block
     */
    public void removeFriend(Email email, boolean block);

    /**
     * Remove friend from one group.
     * 
     * @param email
     * 		email
     * @param groupId
     * 		group id
     */
    public void removeFriend(Email email, String groupId);

    /**
     * Move friend from one group to other group.
     * 
     * @param email
     * 		email
     * @param srcGroupId
     * 		source group id 
     * @param destGroupId
     * 		dest group id
     */
    public void moveFriend(Email email, String srcGroupId, String destGroupId);

    /**
     * Block friend.
     * 
     * @param email
     * 		email
     */
    public void blockFriend(Email email);

    /**
     * Unblock friend.
     * 
     * @param email
     * 		email
     */
    public void unblockFriend(Email email);

    /**
     * Rename friend.
     * 
     * @param email
     * 		email
     * @param friendlyName
     * 		new friendly name
     */
    public void renameFriend(Email email, String friendlyName);

}
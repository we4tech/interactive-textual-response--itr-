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

import java.io.File;

import net.sf.jml.message.MsnMimeMessage;
import net.sf.jml.protocol.MsnOutgoingMessage;

/**
 * Msn switchboard, communication with MSN SB server.
 * 
 * @author Roger Chen
 */
public interface MsnSwitchboard {

    /**
     * Get the MsnMessenger belongs to.
     * 
     * @return
     * 		MsnMessenger
     */
    public MsnMessenger getMessenger();

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
     * Get current switchboard's connectin information.
     * 
     * @return
     * 		connection information
     */
    public MsnConnection getConnection();

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
     * Close switchboard.
     */
    public void close();

    /**
     * Get all contacts on the switchboard.
     * 
     * @return
     * 		all contacts on the switchboard.
     */
    public MsnContact[] getAllContacts();

    /**
     * Is contain the contact.
     * 
     * @param email
     * 		email
     * @return
     * 		is contain the contact 
     */
    public boolean containContact(Email email);

    /**
     * Inviate a contact to join the switchboard.
     * 
     * @param email
     * 		contact's email
     */
    public void inviteContact(Email email);

    /**
     * Get all active file transfer.
     * 
     * @return
     * 		all file transfer
     */
    public MsnFileTransfer[] getActiveFileTransfers();

    /**
     * Send file.
     * 
     * @param file
     * 		send file
     * @throws IllegalArgumentException
     * 		if file not existed
     * @throws IllegalStateException
     * 		if no one or more than one contact in current switchboard  
     */
    public void sendFile(File file) throws IllegalArgumentException,
            IllegalStateException;

    /**
     * Send text message without format.
     * 
     * @param text
     * 		text
     */
    public void sendText(String text);

    /**
     * Send message to others. If block, the method will return
     * after the message successfully sent or failed. If not block, the
     * method always return false.
     * 
     * @param message
     * 		MsnMimeMessage
     * @param block
     * 		is block
     * @return
     * 		if block, return message send successful, else return false  
     */
    public boolean sendMessage(MsnMimeMessage message, boolean block);

    /**
     * This method is a shorthand for:
     * <pre>
     *     sendMessage(message, false);
     * </pre>
     * 
     * @param message
     * 		MsnMimeMessage
     */
    public void sendMessage(MsnMimeMessage message);

    /**
     * Send protocol message. If block, the method will return
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

}
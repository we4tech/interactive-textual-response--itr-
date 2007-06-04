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

/**
 * Msn file transfer.
 * 
 * @author Roger Chen
 */
public interface MsnFileTransfer {

    /**
     * Get the MsnSwitchboard the controller belongs to.
     * 
     * @return
     * 		MsnSwitchboard
     */
    public MsnSwitchboard getSwitchboard();

    /**
     * Get the MsnMessenger the controller belongs to.
     * 
     * @return
     * 		MsnMessenger
     */
    public MsnMessenger getMessenger();

    /**
     * Get the contact who sent the file or received the file.
     * 
     * @return
     * 		the contact
     */
    public MsnContact getContact();

    /**
     * Get file transfer state.
     * 
     * @return
     * 		file transfer state
     */
    public MsnFileTransferState getState();

    /**
     * Get the transfer file.
     * 
     * @return
     * 		transfer file
     */
    public File getFile();

    /**
     * Set the transfer file only if the transfer controller not started.
     * 
     * @param file
     * 		transfer file
     * @throws IllegalStateException
     * 		if the transfer started
     */
    public void setFile(File file) throws IllegalStateException;

    /**
     * Get the total size of the file.
     * 
     * @return
     * 		the file total size
     */
    public long getFileTotalSize();

    /**
     * Get transferred size.
     * 
     * @return
     * 		the transferred size
     */
    public long getTransferredSize();

    /**
     * Is started.
     * 
     * @return
     * 		is started
     */
    public boolean isStarted();

    /**
     * Start transfer.
     */
    public void start();

    /**
     * Cancel transfer.
     */
    public void cancel();

    /**
     * Is the MsnOwner send file to others.
     * 
     * @return
     * 		is the MsnOwner send file to others
     */
    public boolean isSender();

    /**
     * Is the MsnOwner receive file from others.
     * 
     * @return
     * 		is the MsnOwner receive file from others
     */
    public boolean isReceiver();

}
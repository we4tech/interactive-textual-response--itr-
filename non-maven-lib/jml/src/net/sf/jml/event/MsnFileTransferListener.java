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

import net.sf.jml.MsnFileTransfer;

/**
 * File transfer listener. 
 * 
 * @author Roger Chen
 */
public interface MsnFileTransferListener {

    /**
     * Received file transfer request. If accept file transfer, call
     * transfer.start(), or call transfer.cancel() to cancel file transfer.
     * You can change the file savepath by call transfer.setFile(File).
     * 
     * @param transfer
     * 		file transfer
     */
    public void fileTransferRequestReceived(MsnFileTransfer transfer);

    /**
     * File transfer started.
     * 
     * @param transfer
     * 		file transfer
     */
    public void fileTransferStarted(MsnFileTransfer transfer);

    /**
     * File transfer process. 
     * 
     * @param transfer
     *      file transfer
     */
    public void fileTransferProcess(MsnFileTransfer transfer);

    /**
     * File transfer finished.  
     * 
     * @param transfer
     * 		file transfer
     */
    public void fileTransferFinished(MsnFileTransfer transfer);
}
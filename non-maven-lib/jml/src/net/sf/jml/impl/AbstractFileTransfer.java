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
package net.sf.jml.impl;

import java.io.File;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnFileTransfer;
import net.sf.jml.MsnFileTransferState;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnSwitchboard;

/**
 * Implement MsnFileTransfer basic method.
 * 
 * @author Roger Chen
 */
public abstract class AbstractFileTransfer implements MsnFileTransfer {

    private final MsnSwitchboard switchboard;
    private final MsnContact contact;

    private MsnFileTransferState state = MsnFileTransferState.INIT;
    private File file;
    private long fileTotalSize = -1;
    private long transferredSize = 0;

    public AbstractFileTransfer(MsnSwitchboard switchboard, MsnContact contact) {
        this.switchboard = switchboard;
        this.contact = contact;
    }

    public MsnSwitchboard getSwitchboard() {
        return switchboard;
    }

    public MsnMessenger getMessenger() {
        return switchboard.getMessenger();
    }

    public MsnContact getContact() {
        return contact;
    }

    public MsnFileTransferState getState() {
        return state;
    }

    public boolean isReceiver() {
        return !isSender();
    }

    public long getFileTotalSize() {
        return fileTotalSize;
    }

    public long getTransferredSize() {
        return transferredSize;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) throws IllegalStateException {
        if (file != null) {
            if (isStarted())
                throw new IllegalStateException(
                        "can't set file after transfer started");
            this.file = file;
        }
    }

    public void setFileTotalSize(long fileTotalSize) {
        this.fileTotalSize = fileTotalSize;
    }

    protected void setTransferredSize(long transferredSize) {
        this.transferredSize = transferredSize;
    }

    protected void setState(MsnFileTransferState state) {
        this.state = state;
    }
}
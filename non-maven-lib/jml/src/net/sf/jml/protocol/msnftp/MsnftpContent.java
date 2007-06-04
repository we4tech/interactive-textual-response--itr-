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
package net.sf.jml.protocol.msnftp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.sf.jml.MsnProtocol;

/**
 * File transfer binary data.
 * <p>
 * Syntax: cancelled(1 byte) binaryDataLen(2 bytes) BinaryData
 * <p>
 * Supported Protocol: MSNC0
 * 
 * @author Roger Chen
 */
public class MsnftpContent extends MsnftpMessage {

    private boolean cancelled;
    private byte[] transferData;

    public MsnftpContent(MsnProtocol protocol) {
        super(protocol);
    }

    public byte[] getTransferData() {
        return transferData;
    }

    public void setTransferData(ByteBuffer transferData) {
        byte[] b = new byte[transferData.remaining()];
        transferData.slice().get(b);
        setTransferData(b);
    }

    public void setTransferData(byte[] b) {
        if (b.length > 0xffff) {
            throw new IllegalArgumentException(
                    "transfer data can't more than 65535 byte");
        }
        this.transferData = b;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    protected boolean load(ByteBuffer buffer) {
        if (buffer.remaining() >= 3) {
            ByteBuffer sliceBuffer = buffer.slice();
            sliceBuffer.order(ByteOrder.LITTLE_ENDIAN);
            cancelled = sliceBuffer.get() == 1;
            int len = sliceBuffer.getShort() & 0xFFFF;

            if (buffer.remaining() < len + 3) { //the message have not finished 
                return false;
            }

            transferData = new byte[len];
            buffer.position(buffer.position() + 3);
            buffer.get(transferData);
            return true;
        }
        return false;
    }

    protected ByteBuffer[] save() {
        ByteBuffer[] buffer = new ByteBuffer[2];
        buffer[0] = ByteBuffer.allocate(3).order(ByteOrder.LITTLE_ENDIAN);
        int len = transferData == null ? 0 : transferData.length;
        buffer[0].put(cancelled ? (byte) 1 : (byte) 0);
        buffer[0].putShort((short) len);
        buffer[0].flip();
        buffer[1] = transferData == null ? ByteBuffer.allocate(0) : ByteBuffer
                .wrap(transferData);
        return buffer;
    }

    public String toString() {
        return getClass().getName() + " Content Length : "
                + (transferData == null ? 0 : transferData.length)
                + (cancelled ? " Cancelled" : "");
    }

    protected void messageReceived(MsnftpSession session) {
        super.messageReceived(session);
        if (session.getFileTransfer().isReceiver()) {
            //TODO
        } else {
            session.close();
        }
    }

    protected void messageSent(MsnftpSession session) {
        super.messageSent(session);
        //TODO
    }

}
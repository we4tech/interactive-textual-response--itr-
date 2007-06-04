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
package net.sf.jml.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;

/**
 * @author Roger Chen
 */
public class GUID {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String LOCALHOST;

    static {
        String host = "";
        try {
            host = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException e) {
        }

        LOCALHOST = host;
        SECURE_RANDOM.nextInt();
    }

    private int hash;

    private int timeLow;
    private short timeMid;
    private short timeHiAndVersion;
    private byte clockSeqHiAndReserved;
    private byte clockSeqLow;
    private byte node0;
    private byte node1;
    private byte node2;
    private byte node3;
    private byte node4;
    private byte node5;

    private GUID() {
    }

    public byte[] toByteArray() {
        byte[] b = new byte[16];
        ByteBuffer buffer = ByteBuffer.wrap(b);
        buffer.putInt(timeLow);
        buffer.putShort(timeMid);
        buffer.putShort(timeHiAndVersion);
        buffer.put(clockSeqHiAndReserved);
        buffer.put(clockSeqLow);
        buffer.put(node0);
        buffer.put(node1);
        buffer.put(node2);
        buffer.put(node3);
        buffer.put(node4);
        buffer.put(node5);
        return b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GUID)) {
            return false;
        }
        GUID guid = (GUID) obj;
        return guid.clockSeqHiAndReserved == clockSeqHiAndReserved
                && guid.clockSeqLow == clockSeqLow && guid.node0 == node0
                && guid.node1 == node1 && guid.node2 == node2
                && guid.node3 == node3 && guid.node4 == node4
                && guid.node5 == node5
                && guid.timeHiAndVersion == timeHiAndVersion
                && guid.timeLow == timeLow && guid.timeMid == timeMid;
    }

    public int hashCode() {
        if (hash == 0) {
            hash = 17;
            hash = 31 * hash + timeLow;
            hash = 31 * hash + timeMid;
            hash = 31 * hash + timeHiAndVersion;
            hash = 31 * hash + node0;
            hash = 31 * hash + node1;
            hash = 31 * hash + node2;
            hash = 31 * hash + node3;
            hash = 31 * hash + node4;
            hash = 31 * hash + node5;
            hash = 31 * hash + clockSeqHiAndReserved;
            hash = 31 * hash + clockSeqLow;
        }
        return hash;
    }

    public String toString() {
        byte[] b = new byte[16];
        ByteBuffer buffer = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(timeLow);
        buffer.putShort(timeMid);
        buffer.putShort(timeHiAndVersion);
        buffer.put(clockSeqHiAndReserved);
        buffer.put(clockSeqLow);
        buffer.put(node0);
        buffer.put(node1);
        buffer.put(node2);
        buffer.put(node3);
        buffer.put(node4);
        buffer.put(node5);

        StringBuffer stringBuffer = new StringBuffer(36);
        stringBuffer.append(NumberUtils.toHexValue(ByteBuffer.wrap(b, 0, 4)));
        stringBuffer.append("-");
        stringBuffer.append(NumberUtils.toHexValue(ByteBuffer.wrap(b, 4, 2)));
        stringBuffer.append("-");
        stringBuffer.append(NumberUtils.toHexValue(ByteBuffer.wrap(b, 6, 2)));
        stringBuffer.append("-");
        stringBuffer.append(NumberUtils.toHexValue(ByteBuffer.wrap(b, 8, 2)));
        stringBuffer.append("-");
        stringBuffer.append(NumberUtils.toHexValue(ByteBuffer.wrap(b, 10, 6)));
        return stringBuffer.toString().toUpperCase();
    }

    public static GUID parseGUID(byte[] b) {
        if (b == null || b.length != 16)
            return null;

        ByteBuffer buffer = ByteBuffer.wrap(b);

        GUID guid = new GUID();
        guid.timeLow = buffer.getInt();
        guid.timeMid = buffer.getShort();
        guid.timeHiAndVersion = buffer.getShort();
        guid.clockSeqHiAndReserved = buffer.get();
        guid.clockSeqLow = buffer.get();
        guid.node0 = buffer.get();
        guid.node1 = buffer.get();
        guid.node2 = buffer.get();
        guid.node3 = buffer.get();
        guid.node4 = buffer.get();
        guid.node5 = buffer.get();
        return guid;
    }

    public static GUID parseGUID(ByteBuffer buffer) {
        if (buffer != null && buffer.remaining() >= 16) {
            byte[] b = new byte[16];
            buffer.get(b);
            return parseGUID(b);
        }
        return null;
    }

    public static GUID parseGUID(String s) {
        if (s == null || s.length() != 36)
            return null;
        char split = '-';
        if ((s.charAt(8) != split) || (s.charAt(13) != split)
                || (s.charAt(18) != split) || (s.charAt(23) != split))
            return null;

        try {
            byte[] b = new byte[16];
            ByteBuffer buffer = ByteBuffer.wrap(b).order(
                    ByteOrder.LITTLE_ENDIAN);
            buffer.putInt((int) Long.parseLong(s.substring(0, 8), 16));
            buffer.putShort((short) Integer.parseInt(s.substring(9, 13), 16));
            buffer.putShort((short) Integer.parseInt(s.substring(14, 18), 16));
            buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.putShort((short) Integer.parseInt(s.substring(19, 23), 16));
            for (int i = 24; i < 36; i += 2) {
                buffer.put((byte) Short.parseShort(s.substring(i, i + 2), 16));
            }

            return parseGUID(b);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static GUID createRandomGuid() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(LOCALHOST);
        buffer.append(System.currentTimeMillis());
        buffer.append(SECURE_RANDOM.nextInt());
        return parseGUID(DigestUtils.md5(buffer.toString().getBytes()));
    }
}
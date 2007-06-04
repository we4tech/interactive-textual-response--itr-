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
package net.sf.jml.protocol.outgoing;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.sf.jml.MsnProtocol;
import net.sf.jml.protocol.MsnOutgoingMessage;
import net.sf.jml.util.DigestUtils;
import net.sf.jml.util.NumberUtils;

/**
 * The response of server's check connection message.
 * <p>
 * Supported Protocol: All
 * <p>
 * MSNP8/9/10 Syntax: QRY trId productId 32(md5 digests len)\r\n md5(digestNum + productKey)
 * <p>
 * MSNP11 syntax: See http://zoronax.bot2k3.net/msn_beta/
 * <pre>
 * For example£º 
 *   <<< CHL 0 22299241447072743325
 *   >>> QRY trId msmsgs@msnmsgr.com Q1P7W2E4J9R8U3S5 32\r\n20dba7e6e9817826b9cd5240cc8add99
 * </pre>
 * 
 * @author Roger Chen
 */
public class OutgoingQRY extends MsnOutgoingMessage {

    private static final String PRODUCT_ID = "msmsgs@msnmsgr.com";
    private static final String PRODUCT_KEY = "Q1P7W2E4J9R8U3S5";

    private static final String MSNP11_PRODUCT_ID = "PROD0090YUAUV{2B";
    private static final String MSNP11_PRODUCT_KEY = "YMM8C_H7KCQ2S_KL";
    private static final long MSNP11_MAGIC_NUM = 0x0E79A9C1L;

    public OutgoingQRY(MsnProtocol protocol) {
        super(protocol);
        setCommand("QRY");

        addParam(getProductId());
    }

    protected boolean isSupportChunkData() {
        return true;
    }

    public String getProductId() {
        if (protocol.before(MsnProtocol.MSNP11)) {
            return PRODUCT_ID;
        } else
            return MSNP11_PRODUCT_ID;
    }

    public void setDigestNum(String str) {
        setChunkData(calc(str));
    }

    private String calc(String str) {
        if (protocol.before(MsnProtocol.MSNP11))
            return DigestUtils.md5(str + PRODUCT_KEY);

        byte[] md5 = DigestUtils.md5((str + MSNP11_PRODUCT_KEY).getBytes());
        int[] md5Ints = getMd5Ints(md5);
        int[] chlInts = getChlInts(str + MSNP11_PRODUCT_ID);
        long key = getKey(md5Ints, chlInts);
        return getChallenge(key, md5);
    }

    private int[] getMd5Ints(byte[] md5) {
        ByteBuffer buffer = ByteBuffer.wrap(md5).order(ByteOrder.LITTLE_ENDIAN);
        int[] md5ints = new int[4];
        for (int i = 0; i < md5ints.length; i++) {
            md5ints[i] = buffer.getInt() & 0x7fffffff;
        }
        return md5ints;
    }

    private int[] getChlInts(String value) {
        byte[] b = value.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(
                (int) Math.ceil((double) b.length / 8) * 8).order(
                ByteOrder.LITTLE_ENDIAN);
        buffer.put(b);
        while (buffer.hasRemaining())
            buffer.put((byte) '0');
        buffer.flip();

        int[] chlInts = new int[buffer.remaining() / 4];
        for (int i = 0; i < chlInts.length; i++) {
            chlInts[i] = buffer.getInt();
        }
        return chlInts;
    }

    private long getKey(int[] md5Ints, int[] chlInts) {
        long high = 0;
        long low = 0;
        for (int i = 0; i < chlInts.length; i = i + 2) {
            long temp = (((chlInts[i] * MSNP11_MAGIC_NUM) % 0x7FFFFFFF) + high);
            temp = ((temp * md5Ints[0]) + md5Ints[1]) % 0x7FFFFFFF;

            high = (chlInts[i + 1] + temp) % 0x7FFFFFFF;
            high = (md5Ints[2] * high + md5Ints[3]) % 0x7FFFFFFF;

            low = low + high + temp;
        }
        high = (high + md5Ints[1]) % 0x7FFFFFFF;
        low = (low + md5Ints[3]) % 0x7FFFFFFF;

        ByteBuffer buffer = ByteBuffer.allocate(8).order(
                ByteOrder.LITTLE_ENDIAN);
        buffer.putInt((int) high);
        buffer.putInt((int) low);
        buffer.flip();
        buffer.order(ByteOrder.BIG_ENDIAN);
        return buffer.getLong();
    }

    private String getChallenge(long key, byte[] md5) {
        ByteBuffer buffer = ByteBuffer.wrap(md5);
        String first = NumberUtils
                .toHexValue((buffer.getLong() ^ key) & 0xFFFFFFFF);
        String second = NumberUtils
                .toHexValue((buffer.getLong() ^ key) & 0xFFFFFFFF);
        return first + second;
    }

}
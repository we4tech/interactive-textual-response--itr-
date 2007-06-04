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

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Roger Chen
 */
public class DigestUtils {

    private static final Log log = LogFactory.getLog(DigestUtils.class);

    public static byte[] md5(byte[] b) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return digest.digest(b);
        } catch (NoSuchAlgorithmException e) {
            log.error(e, e);
            return null;
        }
    }

    public static String md5(String s) {
        byte[] result = md5(s.getBytes());
        return NumberUtils.toHexValue(ByteBuffer.wrap(result));
    }

    public static byte[] sha1(byte[] b) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return digest.digest(b);
        } catch (NoSuchAlgorithmException e) {
            log.error(e, e);
            return null;
        }
    }

    public static byte[] sha1(ByteBuffer[] buffers) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            for (int i = 0; i < buffers.length; i++) {
                update(digest, buffers[i].slice());
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error(e, e);
            return null;
        }
    }

    private static void update(MessageDigest digest, ByteBuffer buffer) {
        if (buffer.hasArray()) {
            digest.update(buffer.array(), buffer.arrayOffset()
                    + buffer.position(), buffer.remaining());
        } else {
            byte[] b = new byte[buffer.remaining()];
            buffer.get(b);
            digest.update(b);
        }
    }
}
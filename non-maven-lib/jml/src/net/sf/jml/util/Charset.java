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
import java.nio.CharBuffer;

import net.sf.cindy.util.CharsetUtils;

/**
 * Charset encode and decode.
 * 
 * @author Roger Chen
 */
public class Charset {

    private static final CharsetUtils charsetUtils = new CharsetUtils(
            JmlConstants.DEFAULT_ENCODING);

    public static ByteBuffer encode(CharBuffer buffer) {
        return charsetUtils.encode(buffer);
    }

    public static ByteBuffer encode(String s) {
        return charsetUtils.encode(s);
    }

    public static ByteBuffer[] encode(String s, int bufferMaxLength) {
        return charsetUtils.encode(CharBuffer.wrap(s), bufferMaxLength);
    }

    public static byte[] encodeAsByteArray(String s) {
        ByteBuffer buffer = encode(s);
        byte[] b = new byte[buffer.remaining()];
        buffer.get(b);
        return b;
    }

    public static String decode(ByteBuffer buffer) {
        return charsetUtils.decode(buffer).toString();
    }

    public static String decode(byte[] b) {
        return decode(ByteBuffer.wrap(b));
    }

    public static String decode(byte[] b, int offset, int length) {
        return decode(ByteBuffer.wrap(b, offset, length));
    }
}
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

/**
 * Some utils for number
 * 
 * @author Roger Chen
 */
public class NumberUtils {

    public static boolean isDigits(String s) {
        if (s == null || s.length() == 0)
            return false;
        for (int i = 0; i < s.length(); i++)
            if (!Character.isDigit(s.charAt(i)))
                return false;
        return true;
    }

    public static boolean isNumber(String s) {
        if (s == null || s.length() == 0)
            return false;
        boolean foundDot = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c)) {
                if (!foundDot && c == '.')
                    foundDot = true;
                else
                    return false;
            }
        }
        return true;
    }

    public static byte stringToByte(String s, byte defaultValue) {
        try {
            return Byte.parseByte(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static byte stringToByte(String s) {
        return stringToByte(s, (byte) 0);
    }

    public static short stringToShort(String s, short defaultValue) {
        try {
            return Short.parseShort(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static short stringToShort(String s) {
        return stringToShort(s, (short) 0);
    }

    public static int stringToInt(String s, int defaultValue) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int stringToInt(String s) {
        return stringToInt(s, 0);
    }

    public static long stringToLong(String s, long defaultValue) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long stringToLong(String s) {
        return stringToLong(s, 0);
    }

    public static float stringToFloat(String s, float defaultValue) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static float stringToFloat(String s) {
        return stringToFloat(s, 0);
    }

    public static String toHexValue(ByteBuffer buffer) {
        StringBuffer result = new StringBuffer(buffer.remaining() * 2);
        while (buffer.hasRemaining()) {
            String value = Integer.toHexString(buffer.get() & 0xff);
            if (value.length() == 1)
                result.append("0"); //ensure 2 digit
            result.append(value);
        }
        return result.toString();
    }

    public static String toHexValue(long l) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, l);
        return toHexValue(buffer);
    }

    public static String toHexValue(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(0, i);
        return toHexValue(buffer);
    }

    public static String toHexValue(short s) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(0, s);
        return toHexValue(buffer);
    }

    public static String toHexValue(byte b) {
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.put(0, b);
        return toHexValue(buffer);
    }
    
    public static int getIntRandom(int i) {
        java.util.Random r = new java.util.Random(System.currentTimeMillis());
        return r.nextInt(i);
    }

    public static int getIntRandom() {
        return getIntRandom((Integer.MAX_VALUE-10)/2)+5;
    }
}
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.util.BitSet;

import sun.security.action.GetPropertyAction;

/**
 * Some utils for String.
 * 
 * @author Roger Chen
 */
public class StringUtils {

	static BitSet dontNeedEncoding;
    static final int caseDiff = ('a' - 'A');
    static String dfltEncName = null;

    static {
        dontNeedEncoding = new BitSet(256);
        int i;
        for (i = 'a'; i <= 'z'; i++) {
            dontNeedEncoding.set(i);
        }
        for (i = 'A'; i <= 'Z'; i++) {
            dontNeedEncoding.set(i);
        }

        for (i = '0'; i <= '9'; i++) {
            dontNeedEncoding.set(i);
        }

        dontNeedEncoding.set('-');
        dontNeedEncoding.set('_');
        dontNeedEncoding.set('.');
        dontNeedEncoding.set('*');
        dfltEncName = (String) AccessController.doPrivileged(
                new GetPropertyAction("file.encoding")
                      );
    }

    public static String urlEncode(String s) {

        if (s == null) {
            return null;
        }

        String enc = JmlConstants.DEFAULT_ENCODING;

        boolean needToChange = false;
        boolean wroteUnencodedChar = false;
        int maxBytesPerChar = 10; // rather arbitrary limit, but safe for now
        StringBuffer out = new StringBuffer(s.length());
        ByteArrayOutputStream buf = new ByteArrayOutputStream(maxBytesPerChar);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(buf, enc);
        } catch (UnsupportedEncodingException ex) {
            return s;
        }

        for (int i = 0; i < s.length(); i++) {
            int c = (int) s.charAt(i);
            //System.out.println("Examining character: " + c);
            if (dontNeedEncoding.get(c)) {
                out.append((char) c);
                wroteUnencodedChar = true;
            } else {
                // convert to external encoding before hex conversion
                try {
                    if (wroteUnencodedChar) { // Fix for 4407610
                        writer = new OutputStreamWriter(buf, enc);
                        wroteUnencodedChar = false;
                    }
                    writer.write(c);

                    /*
                     * If this character represents the start of a Unicode
                     * surrogate pair, then pass in two characters. It's not
                     * clear what should be done if a bytes reserved in the
                     * surrogate pairs range occurs outside of a legal
                     * surrogate pair. For now, just treat it as if it were
                     * any other character.
                     */

                    if (c >= 0xD800 && c <= 0xDBFF) {
                        /*
                          System.out.println(Integer.toHexString(c)
                          + " is high surrogate");
                         */

                        if ((i + 1) < s.length()) {

                            int d = (int) s.charAt(i + 1);

                            /*
                              System.out.println("\tExamining "
                              + Integer.toHexString(d));
                             */

                            if (d >= 0xDC00 && d <= 0xDFFF) {

                                /*
                                  System.out.println("\t"
                                  + Integer.toHexString(d)
                                  + " is low surrogate");
                                 */

                                writer.write(d);
                                i++;
                            }
                        }
                    }
                    writer.flush();
                } catch (IOException e) {
                    buf.reset();
                    continue;
                }

                byte[] ba = buf.toByteArray();
                for (int j = 0; j < ba.length; j++) {
                    out.append('%');
                    char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);

                    // converting to use uppercase letter as part of
                    // the hex value if ch is a letter.
                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }

                    out.append(ch);
                    ch = Character.forDigit(ba[j] & 0xF, 16);

                    if (Character.isLetter(ch)) {
                        ch -= caseDiff;
                    }

                    out.append(ch);
                }

                buf.reset();
                needToChange = true;
            }
        }

        return (needToChange ? out.toString() : s);
    }

    public static String urlDecode(String text) {
        if (text == null) {
            return null;
        }
        try {
            return URLDecoder.decode(text, JmlConstants.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            return text;
        }
    }

    public static String ltrim(String s) {
        StringBuffer buffer = new StringBuffer();
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = s.charAt(i);
            if (c != ' ') {
                buffer.append(chars, i, chars.length - i);
                break;
            }
        }
        return buffer.toString();
    }

    public static String rtrim(String s) {
        StringBuffer buffer = new StringBuffer();
        char[] chars = s.toCharArray();

        for (int i = chars.length; i > 0; i--) {
            char c = s.charAt(i - 1);
            if (c != ' ') {
                buffer.append(chars, 0, i);
                break;
            }
        }
        return buffer.toString();
    }

    public static String xmlEscaping(String text) {
        text = text.replaceAll("&", "&amp;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        text = text.replaceAll("\"", "&quot;");
        text = text.replaceAll("\'", "&apos;");
        return text;
    }

    public static String debug(ByteBuffer buffer) {
        int rows = (int) Math.ceil((double) buffer.remaining() / 16);
        StringBuffer sb = new StringBuffer(rows * 80);
        int pos = 0;
        for (int i = 0; i < rows; i++) {
            byte[] b = new byte[Math.min(16, buffer.remaining())];
            buffer.get(b);

            sb.append(NumberUtils.toHexValue(pos));
            sb.append("h: ");
            for (int j = 0; j < 16; j++) {
                if (j < b.length)
                    sb.append(NumberUtils.toHexValue(b[j]).toUpperCase());
                else
                    sb.append("  ");
                sb.append(" ");
            }
            sb.append("; ");
            String content = Charset.decode(b);

            char[] c = content.toCharArray();
            for (int j = 0; j < c.length; j++) {
                if (Character.isISOControl(c[j]))
                    sb.append('.');
                else
                    sb.append(c[j]);
            }
            sb.append(JmlConstants.LINE_SEPARATOR);
            pos += 16;
        }
        return sb.toString();
    }
    
    //*********************************************************************
    //* Base64 - a simple base64 encoder and decoder.
     //*
      //*     Copyright (c) 1999, Bob Withers - bwit@pobox.com
       //*
        //* This code may be freely used for any purpose, either personal
         //* or commercial, provided the authors copyright notice remains
          //* intact.
           //*********************************************************************

            /**
             * Encodes a String as a base64 String.
             *
             * @param data a String to encode.
             * @return a base64 encoded String.
             */
            public static String encodeBase64(String data) {
                return encodeBase64(data.getBytes());
            }

   /**
    * Encodes a byte array into a base64 String.
    *
    * @param data a byte array to encode.
    * @return a base64 encode String.
    */
   public static String encodeBase64(byte[] data) {
       int c;
       int len = data.length;
       StringBuffer ret = new StringBuffer(((len / 3) + 1) * 4);
       for (int i = 0; i < len; ++i) {
           c = (data[i] >> 2) & 0x3f;
           ret.append(cvt.charAt(c));
           c = (data[i] << 4) & 0x3f;
           if (++i < len)
               c |= (data[i] >> 4) & 0x0f;

           ret.append(cvt.charAt(c));
           if (i < len) {
               c = (data[i] << 2) & 0x3f;
               if (++i < len)
                   c |= (data[i] >> 6) & 0x03;

               ret.append(cvt.charAt(c));
           } else {
               ++i;
               ret.append((char) fillchar);
           }

           if (i < len) {
               c = data[i] & 0x3f;
               ret.append(cvt.charAt(c));
           } else {
               ret.append((char) fillchar);
           }
       }
       return ret.toString();
   }

   /**
    * Decodes a base64 String.
    *
    * @param data a base64 encoded String to decode.
    * @return the decoded String.
    */
   public static String decodeBase64(String data) {
       return decodeBase64(data.getBytes());
   }

   /**
    * Decodes a base64 aray of bytes.
    *
    * @param data a base64 encode byte array to decode.
    * @return the decoded String.
    */
   public static String decodeBase64(byte[] data) {
       int c, c1;
       int len = data.length;
       StringBuffer ret = new StringBuffer((len * 3) / 4);
       for (int i = 0; i < len; ++i) {
           c = cvt.indexOf(data[i]);
           ++i;
           c1 = cvt.indexOf(data[i]);
           c = ((c << 2) | ((c1 >> 4) & 0x3));
           ret.append((char) c);
           if (++i < len) {
               c = data[i];
               if (fillchar == c)
                   break;

               c = cvt.indexOf((char) c);
               c1 = ((c1 << 4) & 0xf0) | ((c >> 2) & 0xf);
               ret.append((char) c1);
           }

           if (++i < len) {
               c1 = data[i];
               if (fillchar == c1)
                   break;

               c1 = cvt.indexOf((char) c1);
               c = ((c << 6) & 0xc0) | c1;
               ret.append((char) c);
           }
       }
       return ret.toString();
   }

   private static final int fillchar = '=';
   private static final String cvt = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                     + "abcdefghijklmnopqrstuvwxyz"
                                     + "0123456789+/";

}
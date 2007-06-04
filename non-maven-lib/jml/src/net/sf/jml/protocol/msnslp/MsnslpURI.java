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
package net.sf.jml.protocol.msnslp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MSNSLP protocol URL.
 * 
 * @author Roger Chen
 */
public class MsnslpURI {

    private static final Pattern pattern = Pattern.compile("msnmsgr:(.*)");

    public static MsnslpURI parseURI(String s) {
        Matcher matcher = pattern.matcher(s);
        if (matcher.matches())
            return new MsnslpURI(matcher.group(1));
        return null;
    }

    public static MsnslpURI createURI(String email) {
        if (email == null)
            return null;
        return new MsnslpURI(email);
    }

    private String email;

    private MsnslpURI(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getURI() {
        return "msnmsgr:" + email;
    }

    public String toString() {
        return getURI();
    }

}
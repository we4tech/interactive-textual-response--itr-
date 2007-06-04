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
package net.sf.jml;

import java.util.regex.Pattern;

/**
 * Email address.
 * 
 * @author Roger Chen
 */
public final class Email {

    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("\\S+@\\S+\\.\\S+");

    private String emailAddress;

    private Email(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public static Email parseStr(String emailAddress) {
        if (EMAIL_PATTERN.matcher(emailAddress).matches())
            return new Email(emailAddress);
        return null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Email)) {
            return false;
        }
        Email email = (Email) obj;
        return emailAddress == null ? email.emailAddress == null : emailAddress
                .equals(email.emailAddress);
    }

    public int hashCode() {
        return emailAddress.hashCode();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String toString() {
        return emailAddress;
    }
}

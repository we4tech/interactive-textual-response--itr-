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

import net.sf.jml.util.NumberUtils;

/**
 * MSNSLP request.
 * 
 * @author Roger Chen
 */
public class MsnslpRequest extends MsnslpMessage {

    private static Pattern pattern = Pattern
            .compile("(\\S+) (\\S+) MSNSLP/(\\d+)\\.(\\d+)");

    private String requestMethod;
    private String requestURI;

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    protected boolean readFirstLine(String firstLine) {
        Matcher matcher = pattern.matcher(firstLine);
        if (matcher.matches()) {
            setRequestMethod(matcher.group(1));
            setRequestURI(matcher.group(2));
            setMajorVersion(NumberUtils.stringToInt(matcher.group(3)));
            setMinorVersion(NumberUtils.stringToInt(matcher.group(4)));
            return true;
        }
        return false;
    }

    protected void writeFirstLine(StringBuffer buffer) {
        buffer.append(requestMethod).append(" ");
        buffer.append(requestURI).append(" ");
        buffer.append("MSNSLP/").append(getMajorVersion()).append('.').append(
                getMinorVersion());
    }

}
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jml.util.NumberUtils;

/**
 * MSNSLP response.
 * 
 * @author Roger Chen
 */
public class MsnslpResponse extends MsnslpMessage {

    private final static Pattern pattern = Pattern
            .compile("MSNSLP/(\\d+)\\.(\\d+) (\\d+) (.*)");

    private final static Map statusMap = new HashMap();

    static {
        statusMap.put("100", "Trying");
        statusMap.put("180", "Ringing");
        statusMap.put("181", "Call Is Being Forwarded");
        statusMap.put("182", "Queued");
        statusMap.put("183", "Session Progress");

        statusMap.put("200", "OK");

        statusMap.put("300", "Multiple Choices");
        statusMap.put("301", "Moved Permanently");
        statusMap.put("302", "Moved Temporarily");
        statusMap.put("305", "Use Proxy");
        statusMap.put("380", "Alternative Service");

        statusMap.put("400", "Bad Request");
        statusMap.put("401", "Unauthorized");
        statusMap.put("402", "Payment Required");
        statusMap.put("403", "Forbidden");
        statusMap.put("404", "Not Found");
        statusMap.put("405", "Method Not Allowed");
        statusMap.put("406", "Not Acceptable");
        statusMap.put("407", "Proxy Authentication Required");
        statusMap.put("408", "Request Time-out");
        statusMap.put("410", "Gone");
        statusMap.put("413", "Request Entity Too Large");
        statusMap.put("414", "Request-URI Too Large");
        statusMap.put("415", "Unsupported Media Type");
        statusMap.put("416", "Unsupported URI Scheme");
        statusMap.put("420", "Bad Extension");
        statusMap.put("421", "Extension Required");
        statusMap.put("423", "Interval Too Brief");
        statusMap.put("480", "Temporarily Unavailable");
        statusMap.put("481", "Call/Transaction Does Not Exist");
        statusMap.put("482", "Loop Detected");
        statusMap.put("483", "Too Many Hops");
        statusMap.put("484", "Address Incomplete");
        statusMap.put("485", "Ambiguous");
        statusMap.put("486", "Busy Here");
        statusMap.put("487", "Request Terminated");
        statusMap.put("488", "Not Acceptable Here");
        statusMap.put("491", "Request Pending");
        statusMap.put("493", "Undecipherable");

        statusMap.put("500", "Server Internal Error");
        statusMap.put("501", "Not Implemented");
        statusMap.put("502", "Bad Gateway");
        statusMap.put("503", "Service Unavailable");
        statusMap.put("504", "Server Time-out");
        statusMap.put("505", "Version Not Supported");
        statusMap.put("513", "Message Too Large");

        statusMap.put("600", "Busy Everywhere");
        statusMap.put("603", "Decline");
        statusMap.put("604", "Does Not Exist Anywhere");
        statusMap.put("606", "Not Acceptable");
    }

    private int statusCode;
    private String reasonPhrase;

    public MsnslpResponse() {
        setStatusCode(200);
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        String reason = (String) statusMap.get(String.valueOf(statusCode));
        if (reason != null) {
            setReasonPhrase(reason);
        }
    }

    protected boolean readFirstLine(String firstLine) {
        Matcher matcher = pattern.matcher(firstLine);
        if (matcher.matches()) {
            setMajorVersion(NumberUtils.stringToInt(matcher.group(1)));
            setMinorVersion(NumberUtils.stringToInt(matcher.group(2)));
            setStatusCode(NumberUtils.stringToInt(matcher.group(3)));
            setReasonPhrase(matcher.group(4));
            return true;
        }
        return false;
    }

    protected void writeFirstLine(StringBuffer buffer) {
        buffer.append("MSNSLP/").append(getMajorVersion()).append('.').append(
                getMinorVersion()).append(" ");
        buffer.append(statusCode).append(" ");
        buffer.append(reasonPhrase);
    }

}
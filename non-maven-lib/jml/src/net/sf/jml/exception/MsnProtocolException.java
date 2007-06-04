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
package net.sf.jml.exception;

import java.util.HashMap;
import java.util.Map;

import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnOutgoingMessage;
import net.sf.jml.util.JmlConstants;

/**
 * The exception that MSN protocol returned.
 * 
 * @author Roger Chen
 */
public class MsnProtocolException extends JmlException {

    private static Map errorMap = new HashMap();

    static {
        errorMap.put(new Integer(200), "Invalid syntax");
        errorMap.put(new Integer(201), "Invalid parameter");
        errorMap.put(new Integer(205), "Invalid user");
        errorMap.put(new Integer(206), "Domain name missing");
        errorMap.put(new Integer(207), "Already logged in");
        errorMap.put(new Integer(208), "Invalid user-name");
        errorMap.put(new Integer(209), "Nickname change illegal");
        errorMap.put(new Integer(210), "User list full");
        errorMap.put(new Integer(213), "User already or not in group");
        errorMap.put(new Integer(215), "User already on list");
        errorMap.put(new Integer(216), "User not on list");
        errorMap.put(new Integer(217), "User not on-line");
        errorMap.put(new Integer(218), "Already in mode");
        errorMap.put(new Integer(219), "User is in the opposite list");
        errorMap.put(new Integer(223), "Too many groups");
        errorMap.put(new Integer(224), "Invalid group");
        errorMap.put(new Integer(225), "User not in group");
        errorMap.put(new Integer(229), "Group name too long");
        errorMap.put(new Integer(230), "Cannot remove group zero");
        errorMap.put(new Integer(231), "Invalid group");
        errorMap.put(new Integer(280), "Switchboard failed");
        errorMap.put(new Integer(281), "Transfer to switchboard failed");
        errorMap.put(new Integer(300), "Required field missing");
        errorMap.put(new Integer(302), "Not logged in");
        errorMap.put(new Integer(500), "Internal server error");
        errorMap.put(new Integer(501), "Database server error");
        errorMap.put(new Integer(502), "Command disabled");
        errorMap.put(new Integer(510), "File operation failed");
        errorMap.put(new Integer(520), "Memory allocation failed");
        errorMap.put(new Integer(540), "Challenge response failed");
        errorMap.put(new Integer(600), "Server is busy");
        errorMap.put(new Integer(601), "Server is unavailable");
        errorMap.put(new Integer(602), "Peer nameserver is down");
        errorMap.put(new Integer(603), "Database connection failed");
        errorMap.put(new Integer(604), "Server is going down");
        errorMap.put(new Integer(605), "Server unavailable");
        errorMap.put(new Integer(707), "Could not create connection");
        errorMap.put(new Integer(710), "Bad CVR parameters sent");
        errorMap.put(new Integer(711), "Write is blocking");
        errorMap.put(new Integer(712), "Session is overloaded");
        errorMap.put(new Integer(713), "Calling too rapidly");
        errorMap.put(new Integer(714), "Too many sessions");
        errorMap.put(new Integer(715), "Not expected");
        errorMap.put(new Integer(717), "Bad friend file");
        errorMap.put(new Integer(731), "Not expected");
        errorMap.put(new Integer(800), "Changing too rapidly");
        errorMap.put(new Integer(910), "Server too busy");
        errorMap.put(new Integer(911), "Authentication failed");
        errorMap.put(new Integer(912), "Server too busy");
        errorMap.put(new Integer(913), "Not allowed when hiding");
        errorMap.put(new Integer(914), "Server unavailable");
        errorMap.put(new Integer(915), "Server unavailable");
        errorMap.put(new Integer(916), "Server unavailable");
        errorMap.put(new Integer(917), "Authentication failed");
        errorMap.put(new Integer(918), "Server too busy");
        errorMap.put(new Integer(919), "Server too busy");
        errorMap.put(new Integer(920), "Not accepting new user");
        errorMap.put(new Integer(921), "Server too busy");
        errorMap.put(new Integer(922), "Server too busy");
        errorMap
                .put(new Integer(923), "Kids Passport without parental consent");
        errorMap.put(new Integer(924), "Passport account not yet verified");
        errorMap.put(new Integer(928), "Bad ticket");
    }

    private final int errorCode;
    private final MsnIncomingMessage incoming;
    private final MsnOutgoingMessage outgoing;

    public MsnProtocolException(int errorCode, MsnIncomingMessage incoming,
            MsnOutgoingMessage outgoing) {
        this.errorCode = errorCode;
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public MsnIncomingMessage getIncomingMessage() {
        return incoming;
    }

    public MsnOutgoingMessage getOutgoingMessage() {
        return outgoing;
    }

    public String toString() {
        String desc = (String) errorMap.get(new Integer(errorCode));
        if (desc == null) {
            desc = "Unknown error";
        }
        return "ErrorCode " + errorCode + " , " + desc
                + JmlConstants.LINE_SEPARATOR + "outgoing : " + outgoing
                + JmlConstants.LINE_SEPARATOR + "incoming : " + incoming;
    }
}
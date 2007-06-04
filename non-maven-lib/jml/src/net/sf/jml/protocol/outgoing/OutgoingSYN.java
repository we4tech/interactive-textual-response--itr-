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

import net.sf.jml.MsnProtocol;
import net.sf.jml.protocol.MsnOutgoingMessage;

/**
 * Synchronize contact list.
 * <p>
 * Supported Protocol: All
 * <p>
 * MSN8/MSNP9 Syntax: SYN trId versionNum
 * <p>
 * MSNP10 Syntax: SYN trId lastChangeTime lastBeChangedTime 
 * (the first is you change contact list, such as add 
 * a user, the second is someone changed your contact list,
 * such as somebody remove you from his contact list) 
 * 
 * @author Roger Chen
 */
public class OutgoingSYN extends MsnOutgoingMessage {

    public OutgoingSYN(MsnProtocol protocol) {
        super(protocol);
        setCommand("SYN");
    }

    public void setCachedVersion(String version) {
        if (version == null) {
            if (protocol.before(MsnProtocol.MSNP10)) {
                version = "0";
            } else {
                version = "0 0";
            }
        }
        setParam(0, version);
    }

    public String getCachedVersion() {
        return getParam(0);
    }

}
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
 * Retrieves URLs. See <a href="http://www.hypothetic.org/docs/msn/notification/miscellaneous.php">http://www.hypothetic.org/docs/msn/notification/miscellaneous.php</a>
 * "Service URLs (URL)" section.
 *
 * Supported Protocol: All
 *
 * Syntax: URL trId serviceType( parameter)
 * 
 * @author Roger Chen
 */
public class OutgoingURL extends MsnOutgoingMessage {

    public OutgoingURL(MsnProtocol protocol) {
        super(protocol);
        setCommand("URL");
    }

    public void setServiceType(String serviceType) {
        setParam(0, serviceType);
    }

    public String getServiceType() {
        return getParam(0);
    }

    public void setParameter(String parameter) {
        setParam(1, parameter);
    }

    public String getParameter() {
        return getParam(1);
    }
}

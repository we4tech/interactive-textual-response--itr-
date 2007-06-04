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
package net.sf.jml.protocol.incoming;

import net.sf.jml.MsnProtocol;
import net.sf.jml.protocol.MsnIncomingMessage;

/**
 * OutgoingCVR's response message. Return the server recommended 
 * version and the download url.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: CVR trId recommendedVersion recommendedVersion miniVersion downloadURL infoURL
 * 
 * @author Roger Chen
 */
public class IncomingCVR extends MsnIncomingMessage {

    public IncomingCVR(MsnProtocol protocol) {
        super(protocol);
    }

    public String getRecommendedVersion() {
        return getParam(0);
    }

    public String getMinimumVersion() {
        return getParam(2);
    }

    public String getDownloadUrl() {
        return getParam(3);
    }

    public String getInformationUrl() {
        return getParam(4);
    }

}
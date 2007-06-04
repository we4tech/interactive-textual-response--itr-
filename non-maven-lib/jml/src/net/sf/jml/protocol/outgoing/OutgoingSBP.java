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
import net.sf.jml.MsnUserPropertyType;
import net.sf.jml.protocol.MsnOutgoingMessage;

/**
 * Change property for other user.
 * <p>
 * Supported Protocol: >= MSNP11
 * <p>
 * Syntax: SBP trId id propertyType property
 *
 * @author Daniel Henninger
 */
public class OutgoingSBP extends MsnOutgoingMessage {

    public OutgoingSBP(MsnProtocol protocol) {
        super(protocol);
        setCommand("SBP");
    }

    public void setId(String id) {
        setParam(0, id);
    }

    public void setPropertyType(MsnUserPropertyType propertyType) {
        setParam(1, propertyType.toString());
    }

    public void setProperty(String property) {
        setParam(2, property);
    }
}


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
 * Change what to do when someone adds you to their contact
 * list .
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: GTC trId A|N
 * 
 * @author Roger Chen
 */
public class OutgoingGTC extends MsnOutgoingMessage {

    public OutgoingGTC(MsnProtocol protocol) {
        super(protocol);
        setCommand("GTC");
        setNotifyMeWhenSomeoneAddedMe(true);
    }

    public void setNotifyMeWhenSomeoneAddedMe(boolean b) {
        setParam(0, b ? "A" : "N");
    }
}
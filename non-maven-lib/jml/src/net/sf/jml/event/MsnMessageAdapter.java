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
package net.sf.jml.event;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.message.MsnDatacastMessage;
import net.sf.jml.message.MsnInstantMessage;
import net.sf.jml.message.MsnSystemMessage;
import net.sf.jml.message.MsnControlMessage;
import net.sf.jml.message.MsnUnknownMessage;

/**
 * MsnMessageListener adapter.
 * 
 * @author Roger Chen
 */
public class MsnMessageAdapter implements MsnMessageListener {

    public void datacastMessageReceived(MsnSwitchboard switchboard,
            MsnDatacastMessage message, MsnContact contact) {
    }

    public void instantMessageReceived(MsnSwitchboard switchboard,
            MsnInstantMessage message, MsnContact contact) {
    }

    public void systemMessageReceived(MsnMessenger messenger,
            MsnSystemMessage message) {
    }

    public void controlMessageReceived(MsnSwitchboard switchboard,
            MsnControlMessage message, MsnContact contact) {
    }

    public void unknownMessageReceived(MsnSwitchboard switchboard,
            MsnUnknownMessage message, MsnContact contact) {
    }
}
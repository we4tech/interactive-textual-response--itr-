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
package net.sf.jml.example;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.event.MsnMessageAdapter;
import net.sf.jml.message.MsnControlMessage;
import net.sf.jml.message.MsnDatacastMessage;
import net.sf.jml.message.MsnInstantMessage;

/**
 * @author Roger Chen
 */
public class EchoMessenger extends BasicMessenger {

    protected void initMessenger(MsnMessenger messenger) {
        messenger.addMessageListener(new MsnMessageAdapter() {

            public void instantMessageReceived(MsnSwitchboard switchboard,
                    MsnInstantMessage message, MsnContact contact) {
                //text message received
                switchboard.sendMessage(message);
            }

            public void controlMessageReceived(MsnSwitchboard switchboard,
                    MsnControlMessage message, MsnContact contact) {
                //such as typing message and recording message
                switchboard.sendMessage(message);
            }

            public void datacastMessageReceived(MsnSwitchboard switchboard,
                    MsnDatacastMessage message, MsnContact contact) {
                //such as Nudge
                switchboard.sendMessage(message);
            }

        });
    }

}

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
import net.sf.jml.MsnList;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.MsnUserStatus;
import net.sf.jml.event.MsnAdapter;
import net.sf.jml.event.MsnSwitchboardAdapter;
import net.sf.jml.message.MsnControlMessage;
import net.sf.jml.message.MsnInstantMessage;

/**
 * @author Roger Chen
 */
public class PowerfulMessenger extends BasicMessenger {

    protected void initMessenger(MsnMessenger messenger) {
        messenger.addListener(new PowerfulListener());
    }

    private static class PowerfulListener extends MsnAdapter {

        public void contactListInitCompleted(MsnMessenger messenger) {
            //get contacts in allow list
            final MsnContact[] contacts = messenger.getContactList()
                    .getContactsInList(MsnList.AL);

            //we have no resuable switchboard now, so we create new switchboard
            final Object id = new Object();

            messenger.addSwitchboardListener(new MsnSwitchboardAdapter() {

                public void switchboardStarted(MsnSwitchboard switchboard) {
                    if (id != switchboard.getAttachment())
                        return;
                    for (int i = 0; i < contacts.length; i++) {
                        //don't send message to offline contact
                        if (contacts[i].getStatus() != MsnUserStatus.OFFLINE) {
                            switchboard.inviteContact(contacts[i].getEmail());
                        }
                    }
                }

                public void contactJoinSwitchboard(MsnSwitchboard switchboard,
                        MsnContact contact) {
                    if (id != switchboard.getAttachment())
                        return;

                    //typing message
                    MsnControlMessage typingMessage = new MsnControlMessage();
                    typingMessage.setTypingUser(switchboard.getMessenger()
                            .getOwner().getDisplayName());
                    switchboard.sendMessage(typingMessage);

                    //text message
                    MsnInstantMessage message = new MsnInstantMessage();
                    message.setBold(false);
                    message.setItalic(false);
                    message
                            .setFontRGBColor((int) (Math.random() * 255 * 255 * 255));
                    message.setContent("hello, " + contact.getFriendlyName());
                    switchboard.sendMessage(message);
                }

                public void switchboardClosed(MsnSwitchboard switchboard) {
                    switchboard.getMessenger().removeSwitchboardListener(this);
                }

            });
            messenger.newSwitchboard(id);
        }
    }
}

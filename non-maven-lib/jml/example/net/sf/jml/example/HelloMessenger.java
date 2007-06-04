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
import net.sf.jml.MsnUserStatus;
import net.sf.jml.event.MsnContactListAdapter;

/**
 * @author Roger Chen
 */
public class HelloMessenger extends BasicMessenger {

    protected void initMessenger(MsnMessenger messenger) {
        messenger.addContactListListener(new MsnContactListAdapter() {

            public void contactListInitCompleted(MsnMessenger messenger) {
                //get contacts in allow list
                MsnContact[] contacts = messenger.getContactList()
                        .getContactsInList(MsnList.AL);

                for (int i = 0; i < contacts.length; i++) {
                    //don't send message to offline contact
                    if (contacts[i].getStatus() != MsnUserStatus.OFFLINE) {
                        //this is the simplest way to send text
                        messenger.sendText(contacts[i].getEmail(), "hello");
                    }
                }
            }

        });
    }

}

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
package net.sf.jml.impl;

import java.io.File;

import net.sf.jml.Email;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnList;
import net.sf.jml.message.MsnControlMessage;
import net.sf.jml.message.MsnInstantMessage;
import net.sf.jml.protocol.outgoing.OutgoingCAL;

/**
 * @author Roger Chen
 */
public class SimpleSwitchboard extends BasicSwitchboard {

    SimpleSwitchboard(BasicMessenger messenger, boolean createdByOwner,
            String ip, int port) {
        super(messenger, createdByOwner, ip, port);
    }

    public void inviteContact(Email email) {
        MsnContact contact = getMessenger().getContactList().getContactByEmail(
                email);
        if (contact == null || !contact.isInList(MsnList.FL))
            return;
        if (containContact(contact))
            return;
        OutgoingCAL message = new OutgoingCAL(getMessenger()
                .getActualMsnProtocol());
        message.setEmail(email);
        send(message);
    }

    public void sendText(String text) {
        MsnControlMessage typingMessage = new MsnControlMessage();
        typingMessage.setTypingUser(getMessenger().getOwner().getEmail()
                .getEmailAddress());
        sendMessage(typingMessage);

        MsnInstantMessage instanceMessage = new MsnInstantMessage();
        instanceMessage.setContent(text);
        sendMessage(instanceMessage);
    }

    public void sendFile(File file) throws IllegalArgumentException,
            IllegalStateException {
        if (file == null || !file.exists())
            throw new IllegalArgumentException("file " + file + " not existed");
        MsnContact[] contacts = getAllContacts();
        if (contacts.length != 1)
            throw new IllegalStateException(
                    "no one or more than one contact in current switchboard");

        //TODO add file transfer support
        throw new UnsupportedOperationException();
    }
}
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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.sf.jml.Email;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnFileTransfer;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.message.MsnMimeMessage;
import net.sf.jml.protocol.MsnOutgoingMessage;

/**
 * Implement MsnSwitchboard basic method.
 * 
 * @author Roger Chen
 */
public abstract class AbstractSwitchboard implements MsnSwitchboard {

    private final MsnMessenger messenger;
    private Object attachment;

    private final Map contacts = new LinkedHashMap();
    private final Set fileTransfers = new LinkedHashSet();

    public AbstractSwitchboard(MsnMessenger messenger) {
        this.messenger = messenger;
    }

    public MsnMessenger getMessenger() {
        return messenger;
    }

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    public MsnContact[] getAllContacts() {
        synchronized (contacts) {
            MsnContact[] result = new MsnContact[contacts.size()];
            contacts.keySet().toArray(result);
            return result;
        }
    }

    public void addContact(MsnContact contact) {
        if (contact != null
                && contact.getContactList().getMessenger() == getMessenger())
            synchronized (contacts) {
                contacts.put(contact, contact.getEmail());
            }
    }

    public void removeContact(MsnContact contact) {
        synchronized (contacts) {
            contacts.remove(contact);
        }
    }

    public boolean containContact(MsnContact contact) {
        synchronized (contacts) {
            return contacts.keySet().contains(contact);
        }
    }

    public boolean containContact(Email email) {
        synchronized (contacts) {
            return contacts.values().contains(email);
        }
    }

    public MsnFileTransfer[] getActiveFileTransfers() {
        synchronized (fileTransfers) {
            MsnFileTransfer[] trans = new MsnFileTransfer[fileTransfers.size()];
            fileTransfers.toArray(trans);
            return trans;
        }
    }

    public void addFileTransfer(MsnFileTransfer transfer) {
        synchronized (fileTransfers) {
            fileTransfers.add(transfer);
        }
    }

    public void removeFileTransfer(MsnFileTransfer transfer) {
        synchronized (fileTransfers) {
            fileTransfers.remove(transfer);
        }
    }

    public void send(MsnOutgoingMessage message) {
        send(message, false);
    }

    public void sendMessage(MsnMimeMessage message) {
        sendMessage(message, false);
    }
}
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jml.Email;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnContactList;
import net.sf.jml.MsnGroup;
import net.sf.jml.MsnList;
import net.sf.jml.MsnMessenger;
import net.sf.jml.util.JmlConstants;

/**
 * @author Roger Chen
 */
public class MsnContactListImpl implements MsnContactList {

    private final MsnMessenger messenger;
    private final MsnGroupImpl defaultGroup;

    // increase query speed
    private final Map groupsMap = new LinkedHashMap();
    private final Map contactsMap = new LinkedHashMap();
    private MsnGroup[] groups;
    private MsnContact[] contacts;

    private String version;

    /**
     * Contact count, used to judge SYN completed.
     */
    private int contactCount;

    /**
     * Group count, used to judge SYN completed.
     */
    private int groupCount;

    public MsnContactListImpl(MsnMessenger messenger) {
        this.messenger = messenger;
        defaultGroup = new MsnGroupImpl(this, true);
        defaultGroup.setGroupId("0");
        defaultGroup.setGroupName("Default group");
        addGroup(defaultGroup);
    }

    public MsnMessenger getMessenger() {
        return messenger;
    }

    public String getVersion() {
        return version;
    }

    public MsnGroup getDefaultGroup() {
        return defaultGroup;
    }

    public MsnGroup[] getGroups() {
        synchronized (groupsMap) {
            if (groups == null) {
                groups = new MsnGroup[groupsMap.size()];
                groupsMap.values().toArray(groups);
            }
            return groups;
        }
    }

    public MsnGroup getGroup(String groupId) {
        synchronized (groupsMap) {
            return (MsnGroup) groupsMap.get(groupId);
        }
    }

    public MsnContact[] getContacts() {
        synchronized (contactsMap) {
            if (contacts == null) {
                contacts = new MsnContact[contactsMap.size()];
                contactsMap.values().toArray(contacts);
            }
            return contacts;
        }
    }

    public MsnContact getContactById(String id) {
        if (id == null)
            return null;
        MsnContact[] contacts = getContacts();
        for (int i = 0; i < contacts.length; i++) {
            if (contacts[i].getId().equals(id))
                return contacts[i];
        }
        return null;
    }

    public MsnContact getContactByEmail(Email email) {
        synchronized (contactsMap) {
            return (MsnContact) contactsMap.get(email);
        }
    }

    public MsnContact[] getContactsInList(MsnList list) {
        if (list == null)
            return new MsnContact[0];

        List contactsList = new LinkedList();
        MsnContact[] contacts = getContacts();
        for (int i = 0; i < contacts.length; i++) {
            if (contacts[i].isInList(list))
                contactsList.add(contacts[i]);
        }

        contacts = new MsnContact[contactsList.size()];
        contactsList.toArray(contacts);
        return contacts;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void addGroup(MsnGroup group) {
        if (group != null && group.getContactList() == this) {
            synchronized (groupsMap) {
                groupsMap.put(group.getGroupId(), group);
                groups = null;
            }
        }
    }

    public void removeGroup(String groupId) {
        if (groupId != null && !groupId.equals(defaultGroup.getGroupId())) {
            synchronized (groupsMap) {
                if (groupsMap.remove(groupId) != null)
                    groups = null;
            }
        }
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public int getGroupCount() {
        return groupCount;
    }

    /**
     * Return group count in current contact list. When not
     * SYN completed, this number may be different from 
     * getGroupCount().
     * 
     * @return 
     * 		current group count
     */
    public int getCurrentGroupCount() {
        synchronized (groupsMap) {
            return groupsMap.size();
        }
    }

    public void addContact(MsnContact contact) {
        if (contact != null && contact.getContactList() == this) {
            synchronized (contactsMap) {
                contactsMap.put(contact.getEmail(), contact);
                contacts = null;
            }
        }
    }

    public void removeContactById(String id) {
        MsnContact contact = getContactById(id);
        if (contact != null)
            removeContactByEmail(contact.getEmail());
    }

    public void removeContactByEmail(Email email) {
        if (email != null) {
            synchronized (contactsMap) {
                if (contactsMap.remove(email) != null)
                    contacts = null;
            }
        }
    }

    public int getContactCount() {
        return contactCount;
    }

    public void setContactCount(int contactCount) {
        this.contactCount = contactCount;
    }

    /**
     * Return contact count in current contact list. When not
     * SYN completed, this number may be different from 
     * getContactCount().
     * 
     * @return 
     * 		current contact count
     */
    public int getCurrentContactCount() {
        synchronized (contactsMap) {
            return contactsMap.size();
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("MsnContactList: [GroupCount] ").append(groupCount)
                .append(" [ContactCount] ").append(contactCount).append(
                        JmlConstants.LINE_SEPARATOR);
        buffer.append("[Groups]").append(JmlConstants.LINE_SEPARATOR);
        buffer.append(groupsMap.values()).append(JmlConstants.LINE_SEPARATOR);
        buffer.append("[Contacts]").append(JmlConstants.LINE_SEPARATOR);
        buffer.append(contactsMap.values());
        return buffer.toString();
    }
}
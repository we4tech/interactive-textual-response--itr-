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

import java.util.LinkedHashSet;
import java.util.Set;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnContactList;
import net.sf.jml.MsnGroup;

/**
 * @author Roger Chen
 */
public class MsnGroupImpl implements MsnGroup {

    private final MsnContactList contactList;
    private final boolean defaultGroup;

    private String groupId;
    private String groupName;
    private Set contacts = new LinkedHashSet();

    public MsnGroupImpl(MsnContactList contactList) {
        this(contactList, false);
    }

    public MsnGroupImpl(MsnContactList contactList, boolean defaultGroup) {
        this.contactList = contactList;
        this.defaultGroup = defaultGroup;
    }

    public MsnContactList getContactList() {
        return contactList;
    }

    public boolean isDefaultGroup() {
        return defaultGroup;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public MsnContact[] getContacts() {
        synchronized (contacts) {
            MsnContact[] result = new MsnContact[contacts.size()];
            contacts.toArray(result);
            return result;
        }
    }

    public boolean containContact(MsnContact contact) {
        synchronized (contacts) {
            return contacts.contains(contact);
        }
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void addContact(String id) {
        MsnContact contact = getContactList().getContactById(id);
        if (contact != null)
            addContact(contact);
    }

    public void removeContact(String id) {
        MsnContact contact = getContactList().getContactById(id);
        if (contact != null)
            removeContact(contact);
    }

    public void clear() {
        MsnContact[] contacts = getContacts();
        for (int i = 0; i < contacts.length; i++) {
            removeContact(contacts[i]);
        }
    }

    void addContact(MsnContact contactPerson) {
        contacts.add(contactPerson);
        if (!contactPerson.belongGroup(this))
            ((MsnContactImpl) contactPerson).addBelongGroup(this);
    }

    void removeContact(MsnContact contactPerson) {
        if (contacts.remove(contactPerson))
            if (contactPerson.belongGroup(this))
                ((MsnContactImpl) contactPerson).removeBelongGroup(this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MsnGroupImpl)) {
            return false;
        }
        MsnGroupImpl group = (MsnGroupImpl) obj;

        if (defaultGroup != group.defaultGroup)
            return false;
        if (!(groupId == null ? group.groupId == null : groupId
                .equals(group.groupId))) {
            return false;
        }
        return contactList == null ? group.contactList == null : contactList
                .equals(group.contactList);
    }

    public int hashCode() {
        return groupId == null ? 0 : groupId.hashCode();
    }

    public String toString() {
        return "MsnGroup: [GroupId] " + groupId + " [GroupName] " + groupName;
    }
}
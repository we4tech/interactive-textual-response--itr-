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

import net.sf.jml.Email;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnContactList;
import net.sf.jml.MsnGroup;
import net.sf.jml.MsnList;

/**
 * @author Roger Chen
 */
public class MsnContactImpl extends MsnUserImpl implements MsnContact {

    private final MsnContactList contactList;

    private String id;
    private String friendlyName;
    private int listNumber;
    private Set belongGroups = new LinkedHashSet();
    private String personalMessage = "";
    private String currentMedia = "";

    public MsnContactImpl(MsnContactList contactList) {
        this.contactList = contactList;
    }

    public MsnContactList getContactList() {
        return contactList;
    }

    public String getId() {
        return id;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getPersonalMessage() {
        return personalMessage;
    }

    public String getCurrentMedia() {
        return currentMedia;
    }

    public boolean isInList(MsnList list) {
        if (list == null)
            return false;
        return (listNumber & list.getListId()) == list.getListId();
    }

    public MsnGroup[] getBelongGroups() {
        synchronized (belongGroups) {
            MsnGroup[] groups = new MsnGroup[belongGroups.size()];
            belongGroups.toArray(groups);
            return groups;
        }
    }

    public boolean belongGroup(MsnGroup group) {
        synchronized (belongGroups) {
            return belongGroups.contains(group);
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public void setEmail(Email email) {
        super.setEmail(email);
        if (email != null) {
            if (id == null)
                setId(email.getEmailAddress());
            if (friendlyName == null)
                setFriendlyName(email.getEmailAddress());
        }
    }

    public void setPersonalMessage(String msg) {
        this.personalMessage = msg;
    }

    public void setCurrentMedia(String currentMedia) {
        this.currentMedia = currentMedia;
    }

    public int getListNumber() {
        return listNumber;
    }

    public void setListNumber(int listNumber) {
        this.listNumber = listNumber;
    }

    public void setInList(MsnList list, boolean b) {
        if (b) {
            listNumber = listNumber | list.getListId();
        } else {
            listNumber = listNumber & ~list.getListId();
        }
    }

    public void addBelongGroup(String groupId) {
        MsnGroup group = getContactList().getGroup(groupId);
        if (group != null)
            addBelongGroup(group);
    }

    public void removeBelongGroup(String groupId) {
        MsnGroup group = getContactList().getGroup(groupId);
        if (group != null)
            removeBelongGroup(group);
    }

    void addBelongGroup(MsnGroup group) {
        belongGroups.add(group);
        if (!group.containContact(this))
            ((MsnGroupImpl) group).addContact(this);
    }

    void removeBelongGroup(MsnGroup group) {
        if (belongGroups.remove(group))
            if (group.containContact(this))
                ((MsnGroupImpl) group).removeContact(this);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MsnContactImpl)) {
            return false;
        }
        MsnContactImpl user = (MsnContactImpl) obj;

        if (!(id == null ? user.id == null : id.equals(user.id)))
            return false;
        return contactList == null ? user.contactList == null : contactList
                .equals(user.contactList);
    }

    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public String toString() {
        return "MsnContact: [ID]" + id + " [Email] " + getEmail()
                + " [DisplayName] " + getDisplayName() + " [FriendlyName] "
                + friendlyName + " [Status] " + getStatus() + " [ListNum] "
                + listNumber;
    }

}
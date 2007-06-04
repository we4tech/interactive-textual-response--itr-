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
import net.sf.jml.MsnGroup;

/**
 * MsnContactListListener adapter.
 * 
 * @author Roger Chen
 */
public class MsnContactListAdapter implements MsnContactListListener {

    public void contactListInitCompleted(MsnMessenger messenger) {
    }

    public void contactListSyncCompleted(MsnMessenger messenger) {
    }

    public void contactStatusChanged(MsnMessenger messenger, MsnContact contact) {
    }

    public void ownerStatusChanged(MsnMessenger messenger) {
    }

    public void contactAddedMe(MsnMessenger messenger, MsnContact contact) {
    }

    public void contactRemovedMe(MsnMessenger messenger, MsnContact contact) {
    }

    public void contactAddCompleted(MsnMessenger messenger, MsnContact contact) {
    }

    public void contactRemoveCompleted(MsnMessenger messenger, MsnContact contact) {
    }

    public void groupAddCompleted(MsnMessenger messenger, MsnGroup group) {
    }

    public void groupRemoveCompleted(MsnMessenger messenger, MsnGroup group) {
    }

}
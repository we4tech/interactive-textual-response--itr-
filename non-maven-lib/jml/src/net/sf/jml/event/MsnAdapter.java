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

import net.sf.jml.*;
import net.sf.jml.message.MsnDatacastMessage;
import net.sf.jml.message.MsnInstantMessage;
import net.sf.jml.message.MsnSystemMessage;
import net.sf.jml.message.MsnControlMessage;
import net.sf.jml.message.MsnUnknownMessage;

/**
 * Implements all listeners which jml provided.
 * 
 * @author Roger Chen
 */
public class MsnAdapter implements MsnContactListListener, MsnMessageListener,
        MsnMessengerListener, MsnSwitchboardListener, MsnFileTransferListener {

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

    public void exceptionCaught(MsnMessenger messenger, Throwable throwable) {
    }

    public void loginCompleted(MsnMessenger messenger) {
    }

    public void logout(MsnMessenger messenger) {
    }

    public void switchboardClosed(MsnSwitchboard switchboard) {
    }

    public void switchboardStarted(MsnSwitchboard switchboard) {
    }

    public void contactJoinSwitchboard(MsnSwitchboard switchboard,
            MsnContact contact) {
    }

    public void contactLeaveSwitchboard(MsnSwitchboard switchboard,
            MsnContact contact) {
    }

    public void fileTransferRequestReceived(MsnFileTransfer transfer) {
    }

    public void fileTransferStarted(MsnFileTransfer transfer) {
    }

    public void fileTransferProcess(MsnFileTransfer transfer) {
    }

    public void fileTransferFinished(MsnFileTransfer transfer) {
    }

}
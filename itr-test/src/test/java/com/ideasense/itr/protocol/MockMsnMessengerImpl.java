/* $Id$ */
/*
 ******************************************************************************
 *   Copyright (C) 2007 IDEASense, (hasin & hasan) 
 *
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Lesser General Public
 *   License as published by the Free Software Foundation; either
 *   version 2.1 of the License, or (at your option) any later version.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 ******************************************************************************
 * $LastChangedBy$
 * $LastChangedDate$
 * $LastChangedRevision$
 ******************************************************************************
*/
package com.ideasense.itr.protocol;

import net.sf.jml.*;
import net.sf.jml.event.*;
import net.sf.jml.protocol.MsnOutgoingMessage;

/**
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class MockMsnMessengerImpl implements MsnMessenger {


  public Object getAttachment() {
    return null;
  }

  public void setAttachment(Object pObject) {

  }

  public boolean isLogIncoming() {
    return false;
  }

  public void setLogIncoming(boolean b) {

  }

  public boolean isLogOutgoing() {
    return false;
  }

  public void setLogOutgoing(boolean b) {

  }

  public MsnProtocol[] getSupportedProtocol() {
    return new MsnProtocol[0];
  }

  public void setSupportedProtocol(MsnProtocol[] pMsnProtocols) {

  }

  public MsnOwner getOwner() {
    return null;
  }

  public MsnContactList getContactList() {
    return null;
  }

  public MsnConnection getConnection() {
    return null;
  }

  public MsnProtocol getActualMsnProtocol() {
    return null;
  }

  public MsnMessageChain getOutgoingMessageChain() {
    return null;
  }

  public MsnMessageChain getIncomingMessageChain() {
    return null;
  }

  public void login() {

  }

  public void logout() {

  }

  public boolean send(MsnOutgoingMessage pMsnOutgoingMessage, boolean b) {
    return false;
  }

  public void send(MsnOutgoingMessage pMsnOutgoingMessage) {

  }

  public void newSwitchboard(Object pObject) {

  }

  public MsnSwitchboard[] getActiveSwitchboards() {
    return new MsnSwitchboard[0];
  }

  public void addListener(MsnAdapter pMsnAdapter) {

  }

  public void addMessengerListener(MsnMessengerListener pMsnMessengerListener) {

  }

  public void removeMessengerListener(MsnMessengerListener pMsnMessengerListener) {

  }

  public void addMessageListener(MsnMessageListener pMsnMessageListener) {

  }

  public void removeMessageListener(MsnMessageListener pMsnMessageListener) {

  }

  public void addContactListListener(MsnContactListListener pMsnContactListListener) {

  }

  public void removeContactListListener(MsnContactListListener pMsnContactListListener) {

  }

  public void addSwitchboardListener(MsnSwitchboardListener pMsnSwitchboardListener) {

  }

  public void removeSwitchboardListener(MsnSwitchboardListener pMsnSwitchboardListener) {

  }

  public void addFileTransferListener(MsnFileTransferListener pMsnFileTransferListener) {

  }

  public void removeFileTransferListener(MsnFileTransferListener pMsnFileTransferListener) {

  }

  public void sendText(Email pEmail, String pString) {

  }

  public void addGroup(String pString) {

  }

  public void removeGroup(String pString) {

  }

  public void renameGroup(String pString, String pString1) {

  }

  public void addFriend(Email pEmail, String pString) {

  }

  public void copyFriend(Email pEmail, String pString) {

  }

  public void removeFriend(Email pEmail, boolean b) {

  }

  public void removeFriend(Email pEmail, String pString) {

  }

  public void moveFriend(Email pEmail, String pString, String pString1) {

  }

  public void blockFriend(Email pEmail) {

  }

  public void unblockFriend(Email pEmail) {

  }

  public void renameFriend(Email pEmail, String pString) {

  }
}

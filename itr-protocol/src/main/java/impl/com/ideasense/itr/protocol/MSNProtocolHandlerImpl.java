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
package impl.com.ideasense.itr.protocol;

import com.ideasense.itr.protocol.AbstractProtocolHandler;
import com.ideasense.itr.common.configuration.ProtocolConfiguration;
import com.ideasense.itr.base.navigation.ITRVisitor;
import com.ideasense.itr.base.navigation.Response;
import com.ideasense.itr.base.service.ObjectInstanceService;
import com.ideasense.itr.base.service.ITRVisitorService;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import net.sf.jml.event.MsnMessengerListener;
import net.sf.jml.event.MsnMessageListener;
import net.sf.jml.event.MsnContactListListener;
import net.sf.jml.*;
import net.sf.jml.util.JmlConstants;
import net.sf.jml.message.*;
import net.sf.jml.impl.MsnMessengerFactory;

import java.net.ProtocolException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of {@code ProtocolHandler} for MSN protocol.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class MSNProtocolHandlerImpl extends AbstractProtocolHandler
    implements MsnMessengerListener, MsnMessageListener, MsnContactListListener
{

  /**
   * Logger instance for the whole class.
   */
  private static final Logger LOG =
      LogManager.getLogger(MSNProtocolHandlerImpl.class);

  private static final String LINE_SEPARATOR = "\r\n";

  /**
   * Unauthorized access message.
   */
  private static final String KEY_UNAUTHORIZED = "unauth.user";
  /**
   * State of debug enable.
   */
  private final boolean DEBUG = LOG.isDebugEnabled();
  /**
   * Flag for maintaining logging status.
   */
  private boolean mLoggedIn = false;
  /**
   * Instance of MsnMessenger implementation.
   */
  private MsnMessenger mMsnMessenger;
  /**
   * Instance of {@code ITRVisitorService} implementation.
   */
  private ITRVisitorService mVisitorService =
      ObjectInstanceService.getVisitorService();

  /**
   * Error message type enumeration.
   */
  private enum ErrorType {
    /**
     * User is not accepted, perhaps user is banned.
     */
    NOT_ACCEPTED
  }

  /**
   * Default constructor.
   * {@inheritDoc}
   *
   * @param pProtocolConfiguration {@inheritDoc}
   */
  public MSNProtocolHandlerImpl(
      final ProtocolConfiguration pProtocolConfiguration) {
    super(pProtocolConfiguration);
    initProtocol();
  }

  /**
   * Inject mock messenger implementation. this method is used only for
   * testing purpose, not valid usages for real usages.
   *
   * @param pMsnMessenger the mock implementation of {@code MsnMessenger}.
   */
  public void setMockMessengerImpl(final MsnMessenger pMsnMessenger) {
    mMsnMessenger = pMsnMessenger;
    subscribeEventListeners();
  }

  /**
   * Initiate protcol initialization process. it includes setting up the
   * credentials and enabeling debugging output.
   */
  private void initProtocol() {
    // perform logon action.
    mMsnMessenger =
        MsnMessengerFactory.createMsnMessenger(
            mProtocolConfiguration.getUserAccount(),
            mProtocolConfiguration.getUserPassword());
    // Enable debugging
    if (DEBUG) {
      mMsnMessenger.setLogIncoming(true);
      mMsnMessenger.setLogOutgoing(true);
    }
    // Set event listeners
    subscribeEventListeners();
  }

  private void subscribeEventListeners() {
    LOG.debug("Setting up event listeners for msn messenger.");
    mMsnMessenger.addMessengerListener(this);
    mMsnMessenger.addMessageListener(this);
    mMsnMessenger.addContactListListener(this);
  }

  //------------------- Messenger state control
  /**
   * set {@code true} to {@code mLoggedIn} flag variable.
   *
   * @param pMsnMessenger the source messenger instance.
   */
  public void loginCompleted(final MsnMessenger pMsnMessenger) {
    if (DEBUG) {
      LOG.debug("Login completed - " + pMsnMessenger);
    }
    mLoggedIn = true;
  }

  /**
   * Set false to mLoggedIn flag variable.
   *
   * @param pMsnMessenger the source messenger instance.
   */
  public void logout(final MsnMessenger pMsnMessenger) {
    if (DEBUG) {
      LOG.debug("Logged out - " + pMsnMessenger);
    }
    mLoggedIn = false;
  }

  /**
   * Fired if any exception caught during messeging process. send the
   * {@code Throwable} the logger instance.
   *
   * @param pMsnMessenger the source messenger instance.
   * @param pThrowable the real exception.
   */
  public void exceptionCaught(final MsnMessenger pMsnMessenger,
                              final Throwable pThrowable) {
    LOG.warn("Exception found during communication.", pThrowable);
  }

  //------------------- Messenger message control.
  public void instantMessageReceived(final MsnSwitchboard pMsnSwitchboard,
                                     final MsnInstantMessage pMessage,
                                     final MsnContact pMsnContact) {
    if (DEBUG) {
      LOG.debug("New instance message received - " + pMessage +
                " from - " + pMsnContact);
    }
    if (pMessage != null) {
      // Find visitor
      final ITRVisitor visitor = ObjectInstanceService.
          getCommandProcessorService().processCommand(
          mProtocolConfiguration, pMsnContact.getId(), pMessage.getContent());

      // Send response to the client messenger.
      sendMessage(visitor, pMsnSwitchboard);
    }
  }

  private void sendMessage(final ITRVisitor pVisitor,
                           final MsnSwitchboard pMsnSwitchboard) {
    if (DEBUG) {
      LOG.debug("Sending message to the visitor - " + pVisitor);
    }
    pMsnSwitchboard.sendText(processResponseText(pVisitor));
  }

  private String processResponseText(final ITRVisitor pVisitor) {
    if (DEBUG) {
      LOG.debug("Process response text for visitor - " + pVisitor);
    }
    // Retrieve Navigation textual result content.
    return ObjectInstanceService.getResponseService().prepareResponse(pVisitor);
  }

  private void sendDenialMessage(final ErrorType pErrorCode,
                                 final MsnSwitchboard pMsnSwitchboard,
                                 final MsnInstantMessage pMessage,
                                 final MsnContact pMsnContact) {
    if (DEBUG) {
      LOG.debug("Sending denial message for - " + pMsnContact.getId());
    }
    switch (pErrorCode) {
      case NOT_ACCEPTED:
        pMsnSwitchboard.sendText(new StringBuilder().
            append(ObjectInstanceService.getText(KEY_UNAUTHORIZED)).
            append(LINE_SEPARATOR).toString());
        break;
    }
  }

  public void controlMessageReceived(final MsnSwitchboard pMsnSwitchboard,
                                     final MsnControlMessage pMessage,
                                     final MsnContact pMsnContact) {
    if (DEBUG) {
      LOG.debug("Control message received - " + pMessage +
                " from - " + pMsnContact);
    }
  }

  public void systemMessageReceived(final MsnMessenger pMsnMessenger,
                                    final MsnSystemMessage pMessage) {
    if (DEBUG) {
      LOG.debug("System message received - " + pMessage);
    }
  }

  public void datacastMessageReceived(final MsnSwitchboard pMsnSwitchboard,
                                      final MsnDatacastMessage pMessage,
                                      final MsnContact pMsnContact) {
    if (DEBUG) {
      LOG.debug("Datacast message received - " + pMsnSwitchboard + " from " +
                pMsnContact);
    }
  }

  public void unknownMessageReceived(final MsnSwitchboard pMsnSwitchboard,
                                     final MsnUnknownMessage pMessage,
                                     final MsnContact pMsnContact) {
    if (DEBUG) {
      LOG.debug("Unknown message received - " + pMessage + " from " +
                pMsnContact);
    }
  }

  //------------------ Msn Contact list listener
  public void contactListSyncCompleted(MsnMessenger pMsnMessenger) {
  }

  public void contactListInitCompleted(MsnMessenger pMsnMessenger) {
  }

  public void contactStatusChanged(MsnMessenger pMsnMessenger,
                                   MsnContact pMsnContact) {
  }

  public void ownerStatusChanged(MsnMessenger pMsnMessenger) {
  }

  public void contactAddedMe(MsnMessenger pMsnMessenger,
                             MsnContact pMsnContact) {
    if (DEBUG) {
      LOG.debug("Add new buddy - " + pMsnContact);
    }
    pMsnMessenger.addFriend(pMsnContact.getEmail(), pMsnContact.getId());
    if (DEBUG) {
      LOG.debug("New buddy added - " + pMsnContact.getId());
    }
  }

  public void contactRemovedMe(MsnMessenger pMsnMessenger,
                               MsnContact pMsnContact) {

  }

  public void contactAddCompleted(MsnMessenger pMsnMessenger,
                                  MsnContact pMsnContact) {

  }

  public void contactRemoveCompleted(MsnMessenger pMsnMessenger,
                                     MsnContact pMsnContact) {

  }

  public void groupAddCompleted(MsnMessenger pMsnMessenger,
                                MsnGroup pMsnGroup) {

  }

  public void groupRemoveCompleted(MsnMessenger pMsnMessenger,
                                   MsnGroup pMsnGroup) {

  }


  //------------------- Daemon control related services.
  /**
   * Request {@code MsnMessenger.login()} to process authentication process.
   *
   * @throws ProtocolException if any exception raised during authentication
   *         process.
   */
  @Override
  public void connectServer() throws ProtocolException {
    mMsnMessenger.login();
  }

  /**
   * Request {@code MsnMessenger.logout()} to process sign off process.
   *
   * @throws ProtocolException if any exception raised during signing off
   *         process.
   */
  @Override
  public void disconnectServer() throws ProtocolException {
    mMsnMessenger.logout();
  }

  /**
   * Return true if protocol is already authenticated and connected.
   *
   * @return true if protocol is already authenticated and connected.
   */
  @Override
  public boolean isConnected() {
    return mLoggedIn;
  }
}

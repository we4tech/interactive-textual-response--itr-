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
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import net.sf.jml.event.MsnMessengerListener;
import net.sf.jml.event.MsnMessageListener;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.MsnContact;
import net.sf.jml.message.*;
import net.sf.jml.impl.MsnMessengerFactory;

import java.net.ProtocolException;

/**
 * Implementation of {@code ProtocolHandler} for MSN protocol.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class MSNProtocolHandlerImpl extends AbstractProtocolHandler
    implements MsnMessengerListener, MsnMessageListener {

  /**
   * Logger instance for the whole class.
   */
  private static final Logger LOG =
      LogManager.getLogger(MSNProtocolHandlerImpl.class);
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
    mMsnMessenger.setLogIncoming(true);
    mMsnMessenger.setLogOutgoing(true);
    // Set event listeners
    subscribeEventListeners();
  }

  private void subscribeEventListeners() {
    LOG.debug("Setting up event listeners for msn messenger.");
    mMsnMessenger.addMessengerListener(this);
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
  public void instantMessageReceived(final MsnSwitchboard pMsnSwitchboard, final MsnInstantMessage pMsnInstantMessage, final MsnContact pMsnContact) {
    if (DEBUG) {
      LOG.debug("New instance message received - " + pMsnInstantMessage +
                " from - " + pMsnContact);
    }
  }

  public void controlMessageReceived(MsnSwitchboard pMsnSwitchboard, MsnControlMessage pMsnControlMessage, MsnContact pMsnContact) {
    if (DEBUG) {
      LOG.debug("Control message received - " + pMsnControlMessage +
                " from - " + pMsnContact);
    }
  }

  public void systemMessageReceived(MsnMessenger pMsnMessenger, MsnSystemMessage pMsnSystemMessage) {
    if (DEBUG) {
      LOG.debug("System message received - " + pMsnSystemMessage);
    }
  }

  public void datacastMessageReceived(MsnSwitchboard pMsnSwitchboard, MsnDatacastMessage pMsnDatacastMessage, MsnContact pMsnContact) {
    if (DEBUG) {
      LOG.debug("Datacast message received - " + pMsnSwitchboard + " from " +
                pMsnContact);
    }
  }

  public void unknownMessageReceived(MsnSwitchboard pMsnSwitchboard, MsnUnknownMessage pMsnUnknownMessage, MsnContact pMsnContact) {
    if (DEBUG) {
      LOG.debug("Unknown message received - " + pMsnUnknownMessage + " from " +
                pMsnContact);
    }
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

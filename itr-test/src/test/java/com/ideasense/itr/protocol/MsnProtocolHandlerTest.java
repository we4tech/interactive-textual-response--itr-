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

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import com.ideasense.itr.base.service.ObjectInstanceService;
import com.ideasense.itr.common.configuration.ProtocolConfiguration;

import java.net.ProtocolException;
import java.lang.reflect.Proxy;

import impl.com.ideasense.itr.protocol.MSNProtocolHandlerImpl;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnContact;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.message.MsnInstantMessage;

/**
 * Test msn implementations.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class MsnProtocolHandlerTest extends MockObjectTestCase {

  private static final Logger LOG = LogManager.getLogger(MsnProtocolHandlerTest.class);
  private MSNProtocolHandlerImpl mMsnProtocolHandler;
  private MockMsnMessengerImpl mMockMsnMessenger;
  private static final String METHOD_GET_ID = "getId";
  private static final String METHOD_SEND_TEXT = "sendText";

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    fixture();
  }

  private void fixture() {
    ProtocolConfiguration configuration = new ProtocolConfiguration();
    configuration.setCompany("ideasense");
    configuration.setType(ProtocolConfiguration.Protocol.MSN);
    configuration.setUserAccount("hasan@somewherein.net");
    configuration.setUserPassword("nhmthk");
    mMsnProtocolHandler =
        (MSNProtocolHandlerImpl) ObjectInstanceService.
            newMsnProtocolHandler(configuration);

    mockMsnMessengerImpl();
  }

  private void mockMsnMessengerImpl() {
    // Create MOck msn messenger
    mMockMsnMessenger = new MockMsnMessengerImpl();
    DebugProxy debugProxy = new DebugProxy(mMockMsnMessenger);
    mMsnProtocolHandler.setMockMessengerImpl(
        (MsnMessenger) Proxy.newProxyInstance(getClass().getClassLoader(),
        mMockMsnMessenger.getClass().getInterfaces(), debugProxy));
  }

  public void testConnect() throws ProtocolException, InterruptedException {
    mMsnProtocolHandler.connectServer();
    assertTrue(mMsnProtocolHandler.isConnected());
    fixSendMessage();
  }

  private void fixSendMessage() {
    final MsnInstantMessage instanceMessage = new MsnInstantMessage();
    instanceMessage.setContent("2");
    final MsnContact msnContact = newMockMsnContact("hasan");
    final MsnSwitchboard msnSwitchboard = newMockMsnSwitchboard();

    mMockMsnMessenger.fireInstantMessageReceived(
        msnSwitchboard, instanceMessage, msnContact);
  }

  private MsnSwitchboard newMockMsnSwitchboard() {
    final MockBuilder<MsnSwitchboard> mockBuilder =
        new MockBuilder<MsnSwitchboard>(MsnSwitchboard.class);
    final MsnSwitchboard msnSwitchboard = mockBuilder.proxy();
    // Set Logic for proxy object
    mockBuilder.registerLogic(METHOD_SEND_TEXT, new MockBuilder.Logic<Object>()
    {
      public Object execute(final Object[] pArgs) {
        LOG.debug("Sending text message.");
        for (Object arg : pArgs) {
          LOG.debug("Arguments - " + arg);
        }
        return null;
      }
    });
    return msnSwitchboard;
  }

  private MsnContact newMockMsnContact(final String pName) {
    final Mock mock = new Mock(MsnContact.class);
    final MsnContact msnContact = (MsnContact) mock.proxy();

    // Set expecptation for 'getId'
    mock.stubs().method(METHOD_GET_ID).withNoArguments().
         will(returnValue(pName));

    return msnContact;
  }
}

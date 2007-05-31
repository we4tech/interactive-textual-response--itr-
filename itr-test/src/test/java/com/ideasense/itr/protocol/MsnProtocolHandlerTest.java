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
import com.ideasense.itr.base.service.ObjectInstanceService;
import com.ideasense.itr.common.configuration.ProtocolConfiguration;

import java.net.ProtocolException;

/**
 * Test msn implementations.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class MsnProtocolHandlerTest extends TestCase {

  private static final Logger LOG = LogManager.getLogger(MsnProtocolHandlerTest.class);
  private ProtocolHandler mMsnProtocolHandler;

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
        ObjectInstanceService.newMsnProtocolHandler(configuration);
    // Set debug proxy
    
  }

  public void testConnect() throws ProtocolException, InterruptedException {
    mMsnProtocolHandler.connectServer();
    Thread.sleep(34000);
  }
}

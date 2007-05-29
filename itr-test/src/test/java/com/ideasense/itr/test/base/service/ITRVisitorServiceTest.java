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
package com.ideasense.itr.test.base.service;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import com.ideasense.itr.base.service.ObjectInstanceService;
import com.ideasense.itr.base.service.NavigationService;
import com.ideasense.itr.base.service.ITRVisitorService;
import com.ideasense.itr.base.service.ResponseService;
import com.ideasense.itr.base.navigation.ITRVisitor;
import com.ideasense.itr.base.navigation.Response;

/**
 * Test interaction between client and service
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ITRVisitorServiceTest extends TestCase {

  private static final Logger LOG =
      LogManager.getLogger(ITRVisitorServiceTest.class);
  private static final String COMPANY_NAME = "ideasense";

  private final ITRVisitorService mItrVisitorService =
      ObjectInstanceService.getVisitorService();
  private final ResponseService mResponseService =
      ObjectInstanceService.getResponseService();

  public void testClient() {
    String visitorCommandText = "";
    ITRVisitor visitor = ObjectInstanceService.newVisitor();
    visitor.setCommand(visitorCommandText);
    LOG.debug("Visitor - " + visitor);

    assertTrue("Visitor is not accepted",
        mItrVisitorService.isVisitorAccepted(COMPANY_NAME, visitor));
    mItrVisitorService.acceptVisitor(COMPANY_NAME, visitor);
    Response response = visitor.getResponse();
    assertNotNull(response);
    LOG.debug("Resonse - " + response);

    // Generate response
    String responseString = mResponseService.prepareResponse(visitor, response);
    assertNotNull(responseString);
    LOG.debug("Response - " + responseString);
  }
}

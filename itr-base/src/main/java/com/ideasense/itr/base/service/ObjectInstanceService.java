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
package com.ideasense.itr.base.service;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import impl.com.ideasense.itr.base.service.ConfigurationServiceImpl;
import impl.com.ideasense.itr.base.service.NavigationServiceImpl;
import impl.com.ideasense.itr.base.service.ITRVisitorServiceImpl;
import impl.com.ideasense.itr.base.service.ResponseServiceImpl;
import impl.com.ideasense.itr.base.navigation.ITRVisitorImpl;
import com.ideasense.itr.base.navigation.ITRVisitor;

/**
 * A singleton container, which create instance of certain services and inject
 * dependecies among the services.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ObjectInstanceService {

  private static final ObjectInstanceService INSTANCE =
      new ObjectInstanceService();

  private final Logger LOG = LogManager.getLogger(getClass());
  private final ConfigurationService mConfigurationService;
  private final NavigationService mNavigationService;
  private final ITRVisitorService mItrVisitorService;
  private final ResponseService mResponseService;


  /**
   * Default constructor, it creates all instance of objects.
   */
  public ObjectInstanceService() {
    LOG.info("Initiating ObjectInstanceService.");
    mConfigurationService = newConfigurationService();
    mNavigationService = newNavigationService();
    mItrVisitorService = newItrVisitorService();
    mResponseService = newResponseService();
  }

  private ResponseService newResponseService() {
    return new ResponseServiceImpl();
  }

  private ITRVisitorService newItrVisitorService() {
    return new ITRVisitorServiceImpl(mNavigationService);
  }

  private NavigationService newNavigationService() {
    return new NavigationServiceImpl(mConfigurationService);
  }

  private ConfigurationService newConfigurationService() {
    return new ConfigurationServiceImpl();
  }

  /**
   * Return implementation instance of {@code ConigurationService}.
   * @return implementation instance of {@code ConfigurationService}.
   */
  public static ConfigurationService getConfigurationService() {
    return INSTANCE.mConfigurationService;
  }

  /**
   * Return Implementation instance of {@code NavigationService}
   * @return implementation instance of {@code NavigationService}
   */
  public static NavigationService getNavigationService() {
    return INSTANCE.mNavigationService;
  }

  /**
   * Return implementation instance of {@code ITRVisitorService}.
   * @return implementation instance of {@code ITRVisitorService}.
   */
  public static ITRVisitorService getVisitorService() {
    return INSTANCE.mItrVisitorService;
  }

  /**
   * Create new instance of {@code ITRVisitor} implementation class.
   * @return new instance of {@code ITRVisitor} implementation class.
   */
  public static ITRVisitor newVisitor() {
    return new ITRVisitorImpl();
  }

  public static ResponseService getResponseService() {
    return INSTANCE.mResponseService;
  }
}

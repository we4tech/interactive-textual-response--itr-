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
package com.ideasense.itr.daemon;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.ideasense.itr.base.service.ConfigurationService;
import com.ideasense.itr.base.service.ObjectInstanceService;
import com.ideasense.itr.base.navigation.ITRMapping;
import com.ideasense.itr.base.navigation.Company;
import com.ideasense.itr.common.configuration.ProtocolConfiguration;
import com.ideasense.itr.protocol.ProtocolHandler;

import java.util.List;
import java.util.ArrayList;
import java.net.ProtocolException;

/**
 * Standalone implementation of {@code DaemonBootstrap} .
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class StandaloneDaemon implements DaemonBootstrap {
  private static final String THREAD_GROUP_NAME_MESSENGER = "messenger";

  private static final Logger LOG =
      LogManager.getLogger(StandaloneDaemon.class);
  private static final long WAKE_UP_AFTER_2_DAYS = 172800000;
  
  private final ConfigurationService mConfigurationService;
  private ITRMapping mItrMapping;
  private List<Company> mCompanies;
  private ThreadGroup mThreadGroup;
  private List<ProtocolHandler> mProtocolHandlers =
      new ArrayList<ProtocolHandler>();

  public StandaloneDaemon() {
    LOG.info("Constructing Standalone Daemon process.");
    mConfigurationService = ObjectInstanceService.getConfigurationService();
  }

  public void initiate() {
    LOG.debug("Initiate ITR mapping.");
    mItrMapping = mConfigurationService.getITRMapping();
    mCompanies = mItrMapping.getCompanies();
    mProtocolHandlers.clear();
  }

  public void start() {
    LOG.info("Start daemon service.");
    // Create thread group for all messenger thread
    mThreadGroup = new ThreadGroup(THREAD_GROUP_NAME_MESSENGER);
    for (final Company company : mCompanies) {
      LOG.info("Initiating ITR service for company - " + company.getName());
      for (ProtocolConfiguration config : company.getProtocolConfigurations()) {
        LOG.info("Starting messenger instance - " + config);
        startMessageInstance(config);
      }
    }
    LOG.info("Daemon service started.");
  }

  private void startMessageInstance(final ProtocolConfiguration pConfig) {
    switch (pConfig.getType()) {
      case MSN:
        LOG.debug("Creating new MSN messenger instance.");
        newThread(ObjectInstanceService.newMsnProtocolHandler(pConfig));
        break;
      case YAHOO:
        throw new RuntimeException("Yahoo protcol is not yet available.");
    }
  }

  private void newThread(final ProtocolHandler pProtocolHandler) {
    Thread newThread = new Thread(mThreadGroup, new Runnable() {
      public void run() {
        LOG.debug("Running new instance of msn messenger.");
        while (true) {
          try {
            pProtocolHandler.connectServer();
            mProtocolHandlers.add(pProtocolHandler);
            try {
              Thread.sleep(WAKE_UP_AFTER_2_DAYS);
            } catch (InterruptedException e) {
              LOG.warn("Thread interrupted", e);
            }
          } catch (Exception e) {
            LOG.warn("Protocol handler failed", e);
          } finally {
            try {
              pProtocolHandler.disconnectServer();
            } catch (ProtocolException e) {
              LOG.warn("Failed to disconnect protocol handler", e);
            }
          }
        }
      }
    });
    newThread.start();
  }

  public void stop() {
    LOG.info("Stop daemon service.");
    mThreadGroup.destroy();
    mProtocolHandlers.clear();
    LOG.info("Daemon service stopped.");
  }
}

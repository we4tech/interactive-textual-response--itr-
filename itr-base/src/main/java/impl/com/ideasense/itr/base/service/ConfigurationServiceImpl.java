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
package impl.com.ideasense.itr.base.service;

import com.ideasense.itr.base.service.ConfigurationService;
import com.ideasense.itr.base.navigation.ITRMapping;
import static com.ideasense.itr.common.configuration.ConfigurationConstant.*;
import com.ideasense.itr.common.helper.ResourceLocator;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import java.util.Properties;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Implementation of {@code ConfigurationService}.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ConfigurationServiceImpl implements ConfigurationService {

  private static final Logger LOG =
      LogManager.getLogger(ConfigurationServiceImpl.class);

  private final Properties mConfigurationLocationProperties = new Properties();
  private final Properties mDaemonProperties = new Properties();
  private ITRMapping mItrMapping;

  /**
   * Default constructor, initiate all properties file.
   */
  public ConfigurationServiceImpl() {
    LOG.debug("Constructing Configuration service implementation.");
    initConfigurationFiles();
  }

  private void initConfigurationFiles() {
    LOG.debug("Initiating configuration files.");
    try {
      initConfigurationLocationProperties();
      initDaemonProperties();
      initITRMapping();
    } catch (IOException e) {
      throw new RuntimeException("Failed to initiate configuration files.", e);
    }
  }

  private void initITRMapping() {
    LOG.debug("Intiiate ITR mapping.");
    final ITRMappingProcessor itrMappingProcessor =
        new ITRMappingProcessor(ResourceLocator.getInputStream(
            getConfigurationLocation(CONFIG_LOCATION_NAVIGATION)));
    mItrMapping = itrMappingProcessor.getITRMapping();
  }

  private void initDaemonProperties() throws IOException {
    LOG.debug("Initiate daemon properties file.");
    final String daemonFile =
        getConfigurationLocation(CONFIG_LOCATION_DAEMON);
    LOG.debug("Daemon file - " + daemonFile);
    final InputStream inputStream = ResourceLocator.getInputStream(daemonFile);
    mDaemonProperties.load(inputStream);
  }

  private void initConfigurationLocationProperties() throws IOException {
    LOG.debug("Initiate configuration location file.");
    final InputStream inputStream =
        ResourceLocator.getInputStream(FILE_CONFIGURATION_INDEX);
    if (inputStream == null) {
      throw new RuntimeException("File - " + FILE_CONFIGURATION_INDEX +
          " not found on class path.");
    }
    mConfigurationLocationProperties.load(inputStream);
  }

  public String getConfigurationLocation(final String pKey) {
    return mConfigurationLocationProperties.getProperty(pKey);
  }

  public String getDaemonProperty(final String pKey) {
    return mDaemonProperties.getProperty(pKey);
  }

  public ITRMapping getITRMapping() {
    return mItrMapping;
  }
}

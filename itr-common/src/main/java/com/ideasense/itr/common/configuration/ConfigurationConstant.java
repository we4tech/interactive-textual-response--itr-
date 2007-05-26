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
package com.ideasense.itr.common.configuration;

/**
 * Define constants for configuration related settings.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ConfigurationConstant {

  /**
   * Configuration index keeping file name.
   */
  public static final String FILE_CONFIGURATION_INDEX =
      "config/configuration-index.properties";

  /**
   * Configuration file location for daemon service is registered on this key.
   */
  public static final String CONFIG_LOCATION_DAEMON = "daemon-config";

  /**
   * Configuration file location for navigation service is registered on this key.
   */
  public static final String CONFIG_LOCATION_NAVIGATION = "navigation-config";
}

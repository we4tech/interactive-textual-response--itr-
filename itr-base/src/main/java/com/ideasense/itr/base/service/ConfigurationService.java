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

import com.ideasense.itr.base.navigation.ITRMapping;

/**
 * Initiate all daemon and protocol handler related configuration.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public interface ConfigurationService {

  /**
   * Find configuration file based on specifiec key and from
   * {@code configuration-index.properties} file.
   * @param pKey file locator key.
   * @return null if nothing found, otherwise the location of file.
   */
  public String getConfigurationLocation(final String pKey);

  /**
   * Open {@code daemon.properties} file and return specific property based on
   * the given key.
   * @param pKey property key.
   * @return the value of property.
   */
  public String getDaemonProperty(final String pKey);

  /**
   * Return the whole ITR (Iteractive Text Response) systems mapping among
   * the companies and navigations.
   * @return return the instance of {@code ITRMapping}
   */
  public ITRMapping getITRMapping();
}

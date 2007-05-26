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

import com.ideasense.itr.base.navigation.ServiceNavigationTree;

import java.util.List;

/**
 * Create service tree from the configuration service.
 * on every client request navigation service will go through all nodes to
 * invoke a specific service.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public interface NavigationService {

  /**
   * Return specific {@code ServiceNavigationTree} by the given company name.
   * @param pCompanyName company name.
   * @return {@code ServiceNavigationTree} is returned. if nothing found null returned.
   */
  public ServiceNavigationTree getServiceTree(final String pCompanyName);

  /**
   * Return a list of available {@code ServiceNavigationTree}.
   * @return a list of available {@code ServiceNavigationTree}.
   */
  public List<ServiceNavigationTree> getAllServiceTrees();
}

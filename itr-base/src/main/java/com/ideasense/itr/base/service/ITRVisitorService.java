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

import com.ideasense.itr.base.navigation.ITRVisitor;

/**
 * Serve ITR Client the navigation service {@code NavigationService}.
 * this class is responsible to visit through all navigation tree based
 * on client response.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public interface ITRVisitorService {

  /**
   * If visitor wasn't banned previously return true.
   * @return true if visitor has right to go through the navigation system.
   * @param pVisitor accept visitor to navigate through all navigation items.
   * @param pCompanyName verify visitor aganist global and company based
   *        banning rule.
   */
  public boolean isVisitorAccepted(final String pCompanyName, 
                                   final ITRVisitor pVisitor);

  /**
   * If visitor has right to visit, let him visit through all tree items.
   * @param pVisitor visitor object.
   * @param pCompanyName company name, where visitor will be accepted to visit.
   */
  public void acceptVisitor(final String pCompanyName,
                            final ITRVisitor pVisitor);

}

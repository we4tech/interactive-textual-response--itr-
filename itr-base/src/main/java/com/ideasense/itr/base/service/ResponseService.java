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

import com.ideasense.itr.base.navigation.Response;
import com.ideasense.itr.base.navigation.ITRVisitor;

/**
 * Prepare original response for client from navigation {@code Response} object.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public interface ResponseService {

  /**
   * This feature will prepare response based on response type. if it is plugin
   * it will talk back to 3rd party plugin provider for content.
   * @param pResponse response from navigaiton service.
   * @return {@code String} response for client.
   * @param pVisitor ITR visitor object.
   */
  public String prepareResponse(final ITRVisitor pVisitor);
}

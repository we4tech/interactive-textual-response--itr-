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
import com.ideasense.itr.common.configuration.ProtocolConfiguration;

/**
 * Process user defined command. there are two kind of commands.<br>
 * 1. navigating service command.<br>
 * 2. system command, which is used to change language type.<br>
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public interface CommandProcessorService {

  /**
   * Process user given message as command. determine system and normal command.
   *
   * @return {@code ITRVisitor} instance is returned in response of
   *         normal command. @param pProtocolConfiguration
   * @param pName  user id.
   * @param pCommand user message.
   * @param pProtocolConfiguration protocol configuration.
   */
  public ITRVisitor processCommand(
      final ProtocolConfiguration pProtocolConfiguration,
      final String pName, final String pCommand);
}

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
package com.ideasense.itr.protocol;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import com.ideasense.itr.common.configuration.ProtocolConfiguration;

import java.net.ProtocolException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Abstract implementation of {@code ProtocolHandler} interface.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public abstract class AbstractProtocolHandler implements ProtocolHandler {

  /**
   * Instantiate {@code Logger} class for the whole child protocol handler.
   */
  protected static final Logger LOG =
      LogManager.getLogger(AbstractProtocolHandler.class);

  /**
   * Protocol configuration, this protected property is recommanded to access
   * from child classes.
   */
  protected final ProtocolConfiguration mProtocolConfiguration;


  /**
   * Default constructor, it accepts {@code ProtocolConfiguration} which
   * holds all configuration related properties.
   * @param pProtocolConfiguration protocol configuration.
   */
  public AbstractProtocolHandler(
      final ProtocolConfiguration pProtocolConfiguration) {
    LOG.info("Initiate AbstractProtocolHandler.");
    if (LOG.isDebugEnabled()) {
      LOG.debug("Protocol configuration - " + pProtocolConfiguration);
    }
    mProtocolConfiguration = pProtocolConfiguration;
  }

  //------------------- Daemon control related services.
  public void connectServer() throws ProtocolException {
    throw new NotImplementedException();
  }

  public void disconnectServer() throws ProtocolException {
    throw new NotImplementedException();
  }

  public boolean isConnected() {
    throw new NotImplementedException();
  }
}

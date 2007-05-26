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

import java.net.ProtocolException;

/**
 * Every protocol implemented classes will use this interface.
 * this interfaces defines the functionalities and start and ending hookup and
 * notifing system. <br>
 * Every protocol handler must be initiated with a default constructor
 * where {@code ConfigurationService} dependency is injected.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public interface ProtocolHandler {

  //------------------- Daemon control related services.
  /**
   * Initiate protocol and attempt to connect remote server.
   * @throws ProtocolException if connection failed throw an exception.
   */
  public void connectServer() throws ProtocolException;

  /**
   * Disconnect protocol handler from the remote server.
   * @throws ProtocolException if failed to release the server connection.
   */
  public void disconnectServer() throws ProtocolException;

  /**
   * If successfully connected, return {@code true} otherwise return
   * {@code false}. <br> this method is used to know preodically know about
   * the live connections. if it returns false this instance of class will
   * be destroyed. and a new connection will be generated.
   * @return true if service is running.
   */
  public boolean isConnected();

}

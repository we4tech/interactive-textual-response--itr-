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

import java.util.Map;

/**
 * Holds the required properties for creating a protocol. this is supplied by
 * the configuraton service during bootstrapping.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ProtocolConfiguration {

  /**
   * Protocol type, probable value - yahoo|msn|google|aim|irc|etc..
   */
  private String type;

  /**
   * Company name, which is used to find out the appropriate navigation service.
   */
  private String company;

  /**
   * User account name, which is used for authentication purpose.
   */
  private String userAccount;

  /**
   * User password, used for authentication purpose.
   */
  private String userPassword;

  /**
   * If connection failed how many times it will attempt for setting up a
   * new connection.
   */
  private Integer reconnectAttempt;

  /**
   * Server host, where this protocol will connect.
   */
  private String serverHost;

  /**
   * Server port, where this protocol will try to conntect.
   */
  private Integer serverPort;

  /**
   * Other configuration related properties.
   */
  private Map<String, String> properties;


  public String getType() {
    return type;
  }

  public void setType(final String pType) {
    type = pType;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(final String pCompany) {
    company = pCompany;
  }

  public String getUserAccount() {
    return userAccount;
  }

  public void setUserAccount(final String pUserAccount) {
    userAccount = pUserAccount;
  }

  public String getUserPassword() {
    return userPassword;
  }

  public void setUserPassword(final String pUserPassword) {
    userPassword = pUserPassword;
  }

  public Integer getReconnectAttempt() {
    return reconnectAttempt;
  }

  public void setReconnectAttempt(final Integer pReconnectAttempt) {
    reconnectAttempt = pReconnectAttempt;
  }

  public String getServerHost() {
    return serverHost;
  }

  public void setServerHost(final String pServerHost) {
    serverHost = pServerHost;
  }

  public Integer getServerPort() {
    return serverPort;
  }

  public void setServerPort(final Integer pServerPort) {
    serverPort = pServerPort;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties(final Map<String, String> pProperties) {
    properties = pProperties;
  }

  /**
   * Send out all properties value from this object.
   * @return
   */
  @Override
  public String toString() {
    StringBuilder settings = new StringBuilder();
    settings.append("{\r\n");
    settings.append("type: '").append(type).append("',\r\n");
    settings.append("userAccount: '").append(userAccount).append("',\r\n");
    settings.append("userPassword: '").append(userPassword).append("',\r\n");
    settings.append("reconnectAttempt: '").append(reconnectAttempt).append("'\r\n");
    settings.append("serverHost: '").append(serverHost).append("',\r\n");
    settings.append("serverPort: '").append(serverPort).append("',\r\n");
    settings.append("properties: '").append(properties).append("'\r\n");
    settings.append("}\r\n");
    return settings.toString();
  }
}

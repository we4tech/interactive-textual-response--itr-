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
package com.ideasense.itr.base.navigation;

import com.ideasense.itr.common.configuration.ProtocolConfiguration;

import java.util.List;
import java.util.ArrayList;

/**
 * Company object for {@code ITRMapping}.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class Company {

  private String mName;
  private String mUrl;
  private ServiceNavigationTree mServiceNavigationTree;
  private List<ProtocolConfiguration> mProtocolConfigurations =
      new ArrayList<ProtocolConfiguration>();

  public String getName() {
    return mName;
  }

  public Company setName(final String pName) {
    mName = pName;
    return this;
  }

  public String getUrl() {
    return mUrl;
  }

  public Company setUrl(final String pUrl) {
    mUrl = pUrl;
    return this;
  }

  public ServiceNavigationTree getServiceNavigationTree() {
    return mServiceNavigationTree;
  }

  public Company setServiceNavigationTree(final ServiceNavigationTree pServiceNavigationTree) {
    mServiceNavigationTree = pServiceNavigationTree;
    return this;
  }

  public List<ProtocolConfiguration> getProtocolConfigurations() {
    return mProtocolConfigurations;
  }

  public Company setProtocolConfigurations(
      final List<ProtocolConfiguration> pProtocolConfigurations) {
    mProtocolConfigurations = pProtocolConfigurations;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("mName: '").append(mName).append("', ");
    builder.append("mUrl: '").append(mUrl).append("', ");
    builder.append("mServiceNavigationTree: '").append(mServiceNavigationTree).
            append("', ");
    builder.append("mProtocolConfigurations: '").append(mProtocolConfigurations).
            append("'");
    builder.append("}");
    return builder.toString();
  }
}

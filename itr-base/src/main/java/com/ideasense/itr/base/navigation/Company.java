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

/**
 * Company object for {@code ITRMapping}.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class Company {

  private String name;
  private String url;
  private ServiceNavigationTree serviceNavigationTree;

  public String getName() {
    return name;
  }

  public void setName(final String pName) {
    name = pName;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(final String pUrl) {
    url = pUrl;
  }

  public ServiceNavigationTree getServiceNavigationTree() {
    return serviceNavigationTree;
  }

  public void setServiceNavigationTree(final ServiceNavigationTree pServiceNavigationTree) {
    serviceNavigationTree = pServiceNavigationTree;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("name: '").append(name).append("',");
    builder.append("url: '").append(url).append("',");
    builder.append("serviceNavigationTree: '").append(serviceNavigationTree).
            append("'");
    builder.append("}");
    return builder.toString();
  }
}

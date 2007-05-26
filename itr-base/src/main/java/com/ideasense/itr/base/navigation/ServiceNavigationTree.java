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
 * A hierarchical presentation of ITR navigation system.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ServiceNavigationTree {

  private Response mWelcomeResponse;
  private NavigableTree<String, Index> mNavigableTree;

  public void setWelcomeMessage(final Response pResponse) {
    mWelcomeResponse = pResponse;
  }

  public Response getWelcomeMessage() {
    return mWelcomeResponse;
  }

  public void setNavigableTree(final NavigableTree<String, Index> pTree) {
    mNavigableTree = pTree;
  }

  public NavigableTree<String, Index> getNavigableTree() {
    return mNavigableTree;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("response: '").append(mWelcomeResponse).append("',");
    builder.append("navigableTree: '").append(mNavigableTree).append("'");
    builder.append("}");
    return builder.toString();
  }
}

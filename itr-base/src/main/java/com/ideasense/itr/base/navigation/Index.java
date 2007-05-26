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
 * Index object, which holds the user interaction.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class Index {

  private String keyCode;
  private String title;
  private Response response;

  public String getKeyCode() {
    return keyCode;
  }

  public void setKeyCode(final String pKeyCode) {
    keyCode = pKeyCode;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String pTitle) {
    title = pTitle;
  }

  public Response getResponse() {
    return response;
  }

  public void setResponse(final Response pResponse) {
    response = pResponse;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("keyCode: ").append(keyCode).append(",");
    builder.append("title: '").append(title).append("',");
    builder.append("response: '").append(response).append("'");
    builder.append("}");
    return builder.toString();
  }
}

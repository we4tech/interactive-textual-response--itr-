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
 * Render different type of textual response. echo|color| etc..
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class Response {

  private Type type;
  private String content;

  public Type getType() {
    return type;
  }

  public void setType(final Type pType) {
    type = pType;
  }

  public String getContent() {
    return content;
  }

  public void setContent(final String pContent) {
    content = pContent;
  }

  public enum Type {
    ECHO, COLOR, MULTILINE, PLUGIN, HTML
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("type: '").append(type).append("',");
    builder.append("content: '").append(content).append("'");
    builder.append("}");
    return builder.toString();
  }
}

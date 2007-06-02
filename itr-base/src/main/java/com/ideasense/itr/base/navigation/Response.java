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

import java.util.Calendar;
import java.util.Locale;

/**
 * Render different type of textual response. echo|color| etc..
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class Response {

  private static final String FORMAT_TIME = "\\$\\{time\\}";
  private static final String FORMAT_DATE = "\\$\\{date\\}";
  private static final String FORMAT_DATE_TIME = "\\${dateTime}";

  private final Calendar mCalendar = Calendar.getInstance();
  private Type type;
  private String content;

  /**
   * Optionally, navigation response content can be set.
   */
  private String navigation;

  public Type getType() {
    return type;
  }

  public void setType(final Type pType) {
    type = pType;
  }

  public String getContent() {
    if (content != null) {
      return formatContent();
    }
    return null;
  }

  private String formatContent() {
    content = content.replaceAll(FORMAT_TIME, mCalendar.getTime().toString());
    content = content.replaceAll(FORMAT_DATE, mCalendar.getTime().toString());
    return content;
  }

  public void setContent(final String pContent) {
    content = pContent;
  }

  public enum Type {
    ECHO, COLOR, MULTILINE, PLUGIN, HTML
  }

  public String getNavigation() {
    return navigation;
  }

  public void setNavigation(final String pNavigation) {
    navigation = pNavigation;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("type: '").append(type).append("',");
    builder.append("content: '").append(content).append("'");
    builder.append("navigation: '\r\n").append(navigation).append("\r\n'");
    builder.append("}");
    return builder.toString();
  }
}

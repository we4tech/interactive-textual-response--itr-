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

import java.util.*;

/**
 * Render different mType of textual response. echo|color| etc..
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class Response {

  private static final String FORMAT_TIME = "\\$\\{time\\}";
  private static final String FORMAT_DATE = "\\$\\{date\\}";
  private static final String FORMAT_DATE_TIME = "\\${dateTime}";
  private static final String DEFAULT_LANG_CODE = "";

  private final Calendar mCalendar = Calendar.getInstance();
  private Type mType;
  /**
   * Store multiple language mContent.
   */
  private Map<String, String> mContent = new HashMap<String, String>();

  /**
   * Optionally, mNavigation response mContent can be set.
   */
  private String mNavigation;
  private String mLanguagecode = DEFAULT_LANG_CODE;

  public Type getType() {
    return mType;
  }

  public void setType(final Type pType) {
    mType = pType;
  }

  public String getContent() {
    final String textContent = mContent.get(mLanguagecode);
    if (textContent != null) {
      return formatContent(textContent);
    }
    return null;
  }

  public String getContent(final String pLangCode) {
    final String textContent = mContent.get(pLangCode);
    if (textContent != null) {
      return formatContent(textContent);
    }
    return null;
  }

  private String formatContent(String pContent) {
    pContent = pContent.replaceAll(FORMAT_TIME, mCalendar.getTime().toString());
    pContent = pContent.replaceAll(FORMAT_DATE, mCalendar.getTime().toString());
    return pContent;
  }

  public void setContent(final String pContent) {
    mContent.put(DEFAULT_LANG_CODE, pContent);
  }

  public void setContent(final String pLangCode, final String pContent) {
    mContent.put(pLangCode, pContent);
  }

  public void setLanguageCode(final String pLanguageCode) {
    mLanguagecode = pLanguageCode;
  }

  public enum Type {
    ECHO, COLOR, MULTILINE, PLUGIN, HTML
  }

  public String getNavigation() {
    return mNavigation;
  }

  public void setNavigation(final String pNavigation) {
    mNavigation = pNavigation;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("mType: '").append(mType).append("',");
    builder.append("mContent: '").append(mContent).append("'");
    builder.append("mNavigation: '\r\n").append(mNavigation).append("\r\n'");
    builder.append("}");
    return builder.toString();
  }
}

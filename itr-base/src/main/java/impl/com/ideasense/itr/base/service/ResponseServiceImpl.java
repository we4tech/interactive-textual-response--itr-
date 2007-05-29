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
package impl.com.ideasense.itr.base.service;

import com.ideasense.itr.base.service.ResponseService;
import com.ideasense.itr.base.navigation.ITRVisitor;
import com.ideasense.itr.base.navigation.Response;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import java.util.List;
import java.net.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of {@code ResponseService}.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ResponseServiceImpl implements ResponseService {

  private final Logger LOG = LogManager.getLogger(ResponseServiceImpl.class);

  public String prepareResponse(final ITRVisitor pVisitor, final Response pResponse) {
    LOG.debug("Preparing response for - " + pVisitor);
    if (pVisitor == null || pResponse == null) {
      throw new IllegalArgumentException("Visitor or Response object must " +
                                         "be not null.");
    }
    // Build response.
    final StringBuilder builder = new StringBuilder();
    // Handle response based on response type.
    switch (pResponse.getType()) {
      case ECHO:
        builder.append("\r\n>>>\t").
                append(formatString(pResponse.getContent())).append("\r\n");
        break;
      case PLUGIN:
        try {
          builder.append(findPluginResponse(pVisitor, pResponse.getContent()));
        } catch (Exception e) {
          LOG.warn("Failed to find response from plugin - ", e);
          builder.append("Error found - ").append(e.getMessage());
        }
        break;
    }

    // if navgation is not empty attach it with the output.
    String navigation = pResponse.getNavigation();
    if (navigation != null) {
      builder.append("-----------------------------\r\n");
      builder.append(navigation);
      builder.append("-----------------------------\r\n");
    }
    return builder.toString();
  }

  private String formatString(final String pContent) {
    // TODO: replace predefined constants;
    return pContent;
  }

  private String findPluginResponse(final ITRVisitor pVisitor,
                                    final String pContent)
      throws URISyntaxException, IOException {
    String buildUrl = pContent + questionOrAnd(pContent) +
                      buildUrlParams(pVisitor.getCommandParams());
    if (LOG.isDebugEnabled()) {
      LOG.debug("Plugin url - " + buildUrl);
    }
    // Send rquest to server
    URI pluginURI = new URI(buildUrl);
    URL pluginUrl = pluginURI.toURL();
    URLConnection urlConnection = pluginUrl.openConnection();
    InputStream inputStream = urlConnection.getInputStream();
    StringBuilder builder = new StringBuilder();
    int c;
    while ((c = inputStream.read()) != -1) {
      builder.append((char) c);
    }
    return builder.toString();
  }

  private String buildUrlParams(final List<String> pCommandParams) {
    if (pCommandParams != null) {
      StringBuilder builder = new StringBuilder();
      for (String param : pCommandParams) {
        builder.append(param).append("&");
      }
      return builder.toString();
    }
    return "";
  }

  private String questionOrAnd(final String pContent) {
    return (pContent.indexOf('?') != -1 ? "&" : "?");
  }
}

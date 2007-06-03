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
import com.ideasense.itr.base.service.ObjectInstanceService;
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
  private static final String LINE_SEPARATOR = "\r\n";
  /**
   * Invalid message language text.
   */
  private static final String KEY_INVALID_MESSAGE = "invalid.message";

  public String prepareResponse(final ITRVisitor pVisitor) {
    LOG.debug("Preparing response for - " + pVisitor);
    final Response response = pVisitor.getResponse();
    if (response == null) {
      throw new IllegalArgumentException("Visitor or Response object must " +
                                         "be not null.");
    }
    // Build response textual content
    final StringBuilder buffer = new StringBuilder();
    // Set welcome message
    buffer.append(pVisitor.getWelcomeMessage().getContent());
    buffer.append(LINE_SEPARATOR);
    // Prepare response message
    if (pVisitor.getCommand() == null) {
      buffer.append(response.getNavigation());
    } else {
      final String content = response.getContent();
      if (content == null) {
        buffer.append(ObjectInstanceService.getText(KEY_INVALID_MESSAGE));
        buffer.append(LINE_SEPARATOR);
        buffer.append(response.getNavigation());
      } else {
        // Handle response based on response type.
        switch (response.getType()) {
          case ECHO:
            buffer.append(response.getContent()).append(LINE_SEPARATOR);
            break;
          case PLUGIN:
            try {
              buffer.append(findPluginResponse(pVisitor,
                                               response.getContent()));
            } catch (Exception e) {
              LOG.warn("Failed to find response from plugin - ", e);
              buffer.append("Error found - ").append(e.getMessage());
            }
            break;
        }
      }
    }
    return buffer.toString();
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
    // Set parameters
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

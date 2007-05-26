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

import com.ideasense.itr.base.navigation.*;
import com.ideasense.itr.common.helper.ResourceLocator;

import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.Collections;
import java.util.TreeMap;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import nu.xom.*;

/**
 * Load ITR Mapping file and create the navigations.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ITRMappingProcessor {

  private final Logger LOG = LogManager.getLogger(getClass());

  private final ITRMapping mItrMapping = new ITRMapping();
  private static final String ELEMENT_COMPANIES = "Companies";
  private static String ElEMENT_COMPANY = "company";
  private static final String ATTR_NAME = "name";
  private static final String ATTR_URL = "url";
  private static final String ATTR_IMPORT = "import";
  private static final String ELEMENT_WELCOME_MESSAGE =
                              "Service-welcome-message";
  private static final String ELEMENT_ECHO = "echo";
  private static final String ELEMENT_SERVICE_INDEX = "Service-index";
  private static final String ELEMENT_INDEX = "Index";
  private static final String ATTR_KEY_CODE = "keyCode";
  private static final String ATTR_TITLE = "title";
  private static final String DEFAULT_INDEX = "0";
  private static final String ELEMENT_PLUG_IN = "plug-in";

  public ITRMappingProcessor(final InputStream pConfigurationInputStream) {
    LOG.debug("Constructing ITRMappingProcessor, configuration stream - " +
              pConfigurationInputStream);

    initiateITRMapping(pConfigurationInputStream);
  }

  private void initiateITRMapping(final InputStream pConfigurationInputStream) {
    try {
      final Builder builder = new Builder();
      Document document = builder.build(pConfigurationInputStream);
      Element rootElement = document.getRootElement(); // ITR-mapping
      // Find out companies
      mItrMapping.setCompanies(findCompanies(rootElement));

    } catch (Exception e) {
      throw new RuntimeException("Failed to initiate ITRMapping", e);
    }
  }

  private List<Company> findCompanies(final Element pRootElement)
      throws IOException, ParsingException {

    Element companiesElement =
        pRootElement.getFirstChildElement(ELEMENT_COMPANIES);
    if (companiesElement != null) {
      Elements companyElements =
          companiesElement.getChildElements(ElEMENT_COMPANY);
      if (companyElements != null) {
        final int companyCount = companyElements.size();
        final List<Company> companyList = new ArrayList<Company>(companyCount);
        for (int i = 0; i < companyCount; i++) {
          final Element companyElement = companyElements.get(i);
          if (companyElement != null) {
            final Company company = new Company();
            company.setName(companyElement.getAttributeValue(ATTR_NAME));
            company.setUrl(companyElement.getAttributeValue(ATTR_URL));
            company.setServiceNavigationTree(
                importNavigation(companyElement.
                                 getAttributeValue(ATTR_IMPORT)));
            companyList.add(company);
          }
        }
        return companyList;
      } else {
        LOG.debug("Element - " + ElEMENT_COMPANY + ", not defined");
      }
    } else {
      LOG.warn("Element - " + ELEMENT_COMPANIES + ", not defined.");
    }
    return Collections.emptyList();
  }

  private ServiceNavigationTree importNavigation(final String pImportPath)
      throws ParsingException, IOException {
    LOG.debug("Importing navigation from - " + pImportPath);
    final InputStream inputStream = ResourceLocator.getInputStream(pImportPath);
    if (inputStream != null) {
      Builder builder = new Builder();
      Document document = builder.build(inputStream);
      return buildServiceNavigationTree(document.getRootElement());
    } else {
      LOG.warn("Failed to import - " + pImportPath + ", " +
               "this file is not under class path.");
    }
    return null;
  }

  private ServiceNavigationTree buildServiceNavigationTree(
      final Element pRootElement) {
    final ServiceNavigationTree serviceNavigationTree =
        new ServiceNavigationTree();
    // Set welcome message
    final Element welcomeMessageElement =
        pRootElement.getFirstChildElement(ELEMENT_WELCOME_MESSAGE);
    if (welcomeMessageElement != null) {
      // Find the response content.
      final Response response = findResponse(welcomeMessageElement);
      serviceNavigationTree.setWelcomeMessage(response);
    } else {
      LOG.warn("Welcome message is not defined.");
    }

    // Set service index
    final Element serviceIndexElement =
        pRootElement.getFirstChildElement(ELEMENT_SERVICE_INDEX);
    if (serviceIndexElement != null) {
      final NavigableTree<String, Index> navigationTree =
          new NavigableTree<String, Index>();
      final NavigableTree.TreeItem<String, Index> pRootItem =
          new NavigableTree.TreeItem<String, Index>();
      pRootItem.setKey(DEFAULT_INDEX);
      pRootItem.setValue(null);
      navigationTree.addRoot(pRootItem);
      recursivlyFindIndex(pRootItem, serviceIndexElement);

      // Set navigation tree.
      serviceNavigationTree.setNavigableTree(navigationTree);
    } else {
      LOG.warn("Service index is not defined.");
    }

    return serviceNavigationTree;
  }

  private void recursivlyFindIndex(
      final NavigableTree.TreeItem<String, Index> pParent,
      final Element pElement) {
    LOG.debug("Recursivly looking for index object");
    if (pElement != null) {
      final Elements indexElements = pElement.getChildElements(ELEMENT_INDEX);
      if (indexElements != null) {
        for (int i = 0; i < indexElements.size(); i++) {
          final Element indexElement = indexElements.get(i);
          final NavigableTree.TreeItem<String, Index> item =
              new NavigableTree.TreeItem<String, Index>();
          final String keyCode = indexElement.getAttributeValue(ATTR_KEY_CODE);
          final String title = indexElement.getAttributeValue(ATTR_TITLE);
          item.setKey(keyCode);
          final Index index = new Index();
          index.setKeyCode(keyCode);
          index.setTitle(title);
          index.setResponse(findResponse(indexElement));
          item.setValue(index);
          pParent.addChildItem(item);
          // Look for more depth value.
          recursivlyFindIndex(item, indexElement);
        }
      }
    }
  }

  private Response findResponse(final Element pElement) {
    final Response response = new Response();
    // find echo
    final Element echoElement = pElement.getFirstChildElement(ELEMENT_ECHO);
    if (echoElement != null) {
      response.setContent(echoElement.getValue());
      response.setType(Response.Type.ECHO);
    } else {
      // find plug-in
      final Element pluginElement =
          pElement.getFirstChildElement(ELEMENT_PLUG_IN);
      if (pluginElement != null) {
        LOG.debug("Plugin element found.");
        String url = pElement.getValue().trim();
        LOG.debug("URL - " + url);
        response.setContent(url);
        response.setType(Response.Type.PLUGIN);
      }
    }

    return response;
  }

  public ITRMapping getITRMapping() {
    return mItrMapping;
  }
}

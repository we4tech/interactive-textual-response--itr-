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
import static com.ideasense.itr.base.navigation.ServiceNavigationTree.*;
import com.ideasense.itr.common.helper.ResourceLocator;
import com.ideasense.itr.common.configuration.ProtocolConfiguration;

import java.io.InputStream;
import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import org.xml.sax.XMLReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;
import nu.xom.*;

/**
 * Load ITR Mapping file and create the navigations.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ITRMappingProcessor {

  private final Logger LOG = LogManager.getLogger(getClass());
  private final boolean DEBUG = LOG.isDebugEnabled();

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
  private static final String ELEMENT_MESSENGER_INSTANCE = "MessengerInstance";
  private static final String ELEMENT_ACCOUNT = "account";
  private static final String ATTR_USER_NAME = "userName";
  private static final String ATTR_PASSWORD = "password";
  private static final String ATTR_TYPE = "type";
  private static final String ELEMENT_SETTINGS = "Settings";
  private static final String ELEMENT_HOST = "host";
  private static final String ATTR_PORT = "port";
  private static final String ELEMENT_PROPERTY = "property";
  private static final String ATTR_VALUE = "value";
  
  private static final String NAMESPACE_ITR_MAPPING =
      "http://idea-sense.com/itr-mapping";
  public static final String SCHEMA_ITR_MAPPING_LOCATION =
      "schema/itr-mapping.xsd";
  private static final String FACTORY_SAX_PARSER =
      "org.apache.xerces.parsers.SAXParser";
  private static final String SCHEMA_VALIDATION =
      "http://apache.org/xml/features/validation/schema";
  private static final String SCHEMA_NAVIGATION_LOCATION =
      "schema/navigation.xsd";
  public static final String NAMESPACE_NAVIGATION =
      "http://idea-sense.com/navigation";

  private final ITRMapping mItrMapping = new ITRMapping();
  private static final String ELEMENT_RESPONSE_MAP = "ResponseMap";
  private static final String ELEMENT_INVALID_COMMAND_ERROR =
      "invalidCommandError";
  private static final String ELEMENT_INTERNAL_ERROR = "internalError";
  private static final String ATTR_LANG = "lang";
  private static final String ELEMENT_HELP_COMMAND = "helpCommand";
  private static final String ELEMENT_SYS_CMD_SUCCESSFUL =
                              "systemCommandSuccessful";
  private static final String ELEMENT_SYS_CMD_FAILED = "systemCommandFailed";

  /**
   * Default constructor, it excepts {@code InputStream} of configuration
   * xml file.
   * @param pConfigurationInputStream input stream of configuration xml file.
   */
  public ITRMappingProcessor(final InputStream pConfigurationInputStream) {
    if (DEBUG) {
      LOG.debug("Constructing ITRMappingProcessor, configuration stream - " +
                pConfigurationInputStream);
    }
    initiateITRMapping(pConfigurationInputStream);
  }

  private void initiateITRMapping(final InputStream pConfigurationInputStream) {
    LOG.info("ITRMapping processing has been started.");
    try {
      Builder builder = new Builder();
      Document document = builder.build(pConfigurationInputStream);
      Element rootElement = document.getRootElement(); // ITR-mapping
      if (DEBUG) {
        LOG.debug("Root element - " + rootElement);
      }
      // Find out companies
      mItrMapping.setCompanies(findCompanies(rootElement));

    } catch (Exception e) {
      throw new RuntimeException("Failed to initiate ITRMapping", e);
    }
  }

  private XMLReader validateXmlDocument(final String pSchemaLocation)
      throws SAXException {
    XMLReader xmlReader =
        XMLReaderFactory.createXMLReader(FACTORY_SAX_PARSER);
    xmlReader.setFeature(SCHEMA_VALIDATION, true);
    xmlReader.setEntityResolver(new EntityResolver() {
      public InputSource resolveEntity(String publicId, String systemId)
          throws SAXException, IOException {
        LOG.info("Resolving entry - " + pSchemaLocation);
        return
            new InputSource(ResourceLocator.getInputStream(pSchemaLocation));
      }
    });
    return xmlReader;
  }

  /**
   * Looking for < company /> elements
   */
  private List<Company> findCompanies(final Element pRootElement)
      throws IOException, ParsingException, SAXException {

    final Element companiesElement =
        pRootElement.getFirstChildElement(ELEMENT_COMPANIES);
    LOG.debug("Companies - " + companiesElement);

    if (companiesElement != null) {
      final Elements companyElements =
          companiesElement.getChildElements(ElEMENT_COMPANY);
      if (companyElements != null) {
        final int companyCount = companyElements.size();
        final List<Company> companyList = new ArrayList<Company>(companyCount);
        for (int i = 0; i < companyCount; i++) {
          final Element companyElement = companyElements.get(i);
          if (companyElement != null) {
            try {
              final Company company = buildCompany(companyElement);
              companyList.add(company);
            } catch (Exception e) {
              LOG.warn("Wrong configuration for company - " +
                       companyElement, e);
            }
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

  private Company buildCompany(final Element pCompanyElement)
      throws IOException, ParsingException {
    if (DEBUG) {
      LOG.debug("Adding company - " + pCompanyElement);
    }
    final Company company = new Company();
    company.setName(pCompanyElement.getAttributeValue(ATTR_NAME));
    company.setUrl(pCompanyElement.getAttributeValue(ATTR_URL));
    company.setProtocolConfigurations(
        findMessengerInstances(company.getName(), pCompanyElement));
    company.setServiceNavigationTree(
        importNavigation(pCompanyElement.
                         getAttributeValue(ATTR_IMPORT)));
    return company;
  }

  /**
   * Looking for < MessengerInstance /> element group, which holds the
   * messenger account credentials.
   */
  private List<ProtocolConfiguration> findMessengerInstances(
      final String pCompanyName, final Element pCompanyElement) {
    LOG.debug("Looking for <MessengerInstance/> from <company/> element.");

    final Element messengerInstanceElement =
        pCompanyElement.getFirstChildElement(ELEMENT_MESSENGER_INSTANCE);
    if (messengerInstanceElement == null) {
      // This very serious error on configuration, let's throw Runtime exception
      throw new RuntimeException("Invalid configuration: <MessengerInstance/> "
                                 + "is not defined on ITR-mapping.xml file.");
    } else {
      LOG.debug("<MessengerInstance/> element found.");

      final Elements accountElements =
          messengerInstanceElement.getChildElements(ELEMENT_ACCOUNT);
      if (accountElements != null && accountElements.size() > 0) {
        final List<ProtocolConfiguration> protocolConfigurations =
            new ArrayList<ProtocolConfiguration>();
        for (int i = 0; i < accountElements.size(); i++) {
          final Element accountElement = accountElements.get(i);
          // Mandatory attributes are -
          // userName, password and type
          final ProtocolConfiguration configuration =
              buildProtocolConfiguration(pCompanyName, accountElement);
          protocolConfigurations.add(configuration);
        }
        return protocolConfigurations;
      } else {
        throw new RuntimeException("Invalid configuration: No <account/> " +
                                   "defined.");
      }
    }
  }

  private ProtocolConfiguration buildProtocolConfiguration(
      final String pCompanyName, final Element pAccountElement) {
    final ProtocolConfiguration configuration = new ProtocolConfiguration();
    configuration.setCompany(pCompanyName).
                  setUserAccount(pAccountElement.
                                 getAttributeValue(ATTR_USER_NAME)).
                  setUserPassword(pAccountElement.
                                  getAttributeValue(ATTR_PASSWORD)).
                  setType(ProtocolConfiguration.Protocol.valueOf(
                          pAccountElement.getAttributeValue(ATTR_TYPE)));
    // Find <Settings/> elements
    findProtocolSettings(pAccountElement, configuration);
    return configuration;
  }

  /**
   * Looking for < Settings /> element under < account /> element. <br>
   * for example: <br>
   * < account userName="" password="" type="MSN">
   *  < Settings >
   *    < host port="212">something.net< /host >
   *    < property name="" value=""/ >
   * < /Settings >
   * < /account >
   */
  private void findProtocolSettings(final Element pAccountElement,
                                    final ProtocolConfiguration pConfiguration)
  {
    LOG.debug("Looking for Protocol <Settings/> element.");

    final Element settingsElement =
        pAccountElement.getFirstChildElement(ELEMENT_SETTINGS);
    if (settingsElement != null) {
      LOG.debug("<Setting/> element found.");

      // Looking for < host/> element
      final Element hostElement =
          settingsElement.getFirstChildElement(ELEMENT_HOST);
      if (hostElement != null) {
        LOG.debug("<host/> element found.");

        pConfiguration.setServerHost(hostElement.getValue());
        final String port = hostElement.getAttributeValue(ATTR_PORT);
        if (port != null) {
          pConfiguration.setServerPort(Integer.valueOf(port));
        }
      }

      // Looking for <property/> element
      final Elements propertyElements =
          settingsElement.getChildElements(ELEMENT_PROPERTY);
      if (propertyElements != null && propertyElements.size() > 0) {
        LOG.debug("<property/> elements found.");

        final Map<String, String> properties = new HashMap<String, String>();
        for (int i = 0; i < propertyElements.size(); i++) {
          final Element propertyElement = propertyElements.get(i);
          properties.put(propertyElement.getAttributeValue(ATTR_NAME),
                         propertyElement.getAttributeValue(ATTR_VALUE));
        }
        pConfiguration.setProperties(properties);
      }
    }
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

    // Find <ResponseMap/>
    findResponseMap(pRootElement, serviceNavigationTree);

    // Find <Service-welcome-message/>
    findWelcomeResponse(pRootElement, serviceNavigationTree);

    // Set service index
    findServiceIndex(pRootElement, serviceNavigationTree);

    return serviceNavigationTree;
  }

  /**
   * Look up for < ErrorResponse/ > element.
   */
  private void findResponseMap(
      final Element pRootElement,
      final ServiceNavigationTree pServiceNavigationTree) {
    LOG.debug("Find <ResponseMap/> element.");
    final Element responseMapElement =
        pRootElement.getFirstChildElement(ELEMENT_RESPONSE_MAP);
    if (responseMapElement != null) {
      LOG.debug("<ResponseMap> element found.");
      final Map<ServiceNavigationTree.ResponseType, Response> errorMap =
          new HashMap<ServiceNavigationTree.ResponseType, Response>();

      // Find <invalidCommandError/>
      final Element invalidCommandErrorElement = responseMapElement.
              getFirstChildElement(ELEMENT_INVALID_COMMAND_ERROR);
      if (invalidCommandErrorElement != null) {
        errorMap.put(ResponseType.INVALID_COMMAND,
                     findResponse(invalidCommandErrorElement));
      }

      // Find <internalError/>
      final Element internalErrorElement =
          responseMapElement.getFirstChildElement(ELEMENT_INTERNAL_ERROR);
      if (internalErrorElement != null) {
        errorMap.put(ResponseType.INTERNAL,
                     findResponse(internalErrorElement));
      }

      // Find <helpCommand/>
      final Element helpCommand =
          responseMapElement.getFirstChildElement(ELEMENT_HELP_COMMAND);
      if (helpCommand != null) {
        errorMap.put(ResponseType.HELP_COMMAND,
                     findResponse(helpCommand));
      }

      // Find <systemCommandSuccessful/>
      final Element systemCommandSuccessfulElement =
          responseMapElement.getFirstChildElement(ELEMENT_SYS_CMD_SUCCESSFUL);
      if (systemCommandSuccessfulElement != null) {
        errorMap.put(ResponseType.SUCCESSFUL_SYSTEM_RESPONSE,
                     findResponse(systemCommandSuccessfulElement));
      }

      // Find <systemCommandFailed/>
      final Element systemCommandFailedElement =
          responseMapElement.getFirstChildElement(ELEMENT_SYS_CMD_FAILED);
      if (systemCommandFailedElement != null) {
        errorMap.put(ResponseType.FAILED_SYSTEM_RESPONSE,
                     findResponse(systemCommandFailedElement));
      }

      // Set on navigation tree
      pServiceNavigationTree.setErrorMessageMap(errorMap);
    } else {
      LOG.warn("No <ErrorResponse/> defined.");
    }
  }

  private void findServiceIndex(
      final Element pRootElement,
      final ServiceNavigationTree pServiceNavigationTree) {
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
      findIndexRecursivly(pRootItem, serviceIndexElement);

      // Set navigation tree.
      pServiceNavigationTree.setNavigableTree(navigationTree);
    } else {
      LOG.warn("Service index is not defined.");
    }
  }

  private void findWelcomeResponse(
      final Element pRootElement,
      final ServiceNavigationTree pServiceNavigationTree) {
    final Element welcomeMessageElement =
        pRootElement.getFirstChildElement(ELEMENT_WELCOME_MESSAGE);
    if (welcomeMessageElement != null) {
      // Find the response content.
      final Response response = findResponse(welcomeMessageElement);
      pServiceNavigationTree.setWelcomeMessage(response);
    } else {
      LOG.warn("Welcome message is not defined.");
    }
  }

  private void findIndexRecursivly(
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
          findIndexRecursivly(item, indexElement);
        }
      }
    }
  }

  private Response findResponse(final Element pElement) {
    if (DEBUG) {
      LOG.debug("Find response object from Element - " + pElement);
    }
    if (pElement != null) {
      final Response response = new Response();
      // find echo
      final Elements echoElements = pElement.getChildElements(ELEMENT_ECHO);
      if (echoElements != null && echoElements.size() > 0) {
        LOG.debug("Response type ECHO found.");

        for (int i = 0; i < echoElements.size(); i++) {
          final Element echoElement = echoElements.get(i);
          final String langAttr = echoElement.getAttributeValue(ATTR_LANG);
          if (langAttr == null) {
            response.setContent(echoElement.getValue());
          } else {
            response.setContent(langAttr, echoElement.getValue());
          }
        }
        response.setType(Response.Type.ECHO);
      } else {
        // find plug-in
        LOG.debug("Looking for <plug-in/> element.");
        final Elements pluginElements =
            pElement.getChildElements(ELEMENT_PLUG_IN);
        if (pluginElements != null && pluginElements.size() > 0) {
          LOG.debug("<plug-in/> element found.");

          for (int i = 0; i < pluginElements.size(); i++) {
            final Element pluginElement = pluginElements.get(i);
            final String langAttr = pluginElement.getAttributeValue(ATTR_LANG);
            final String url = pluginElement.getValue().trim();
            LOG.debug("URL - " + url);

            if (langAttr == null) {
              response.setContent(url);
            } else {
              response.setContent(langAttr, url);
            }
          }
          response.setType(Response.Type.PLUGIN);
        }
      }

      return response;
    }
    return null;
  }

  public ITRMapping getITRMapping() {
    return mItrMapping;
  }
}

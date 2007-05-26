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
package com.ideasense.itr.test.base.service;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;
import com.ideasense.itr.base.service.ConfigurationService;
import com.ideasense.itr.base.service.ObjectInstanceService;
import com.ideasense.itr.base.navigation.*;
import com.ideasense.itr.common.configuration.ConfigurationConstant;

import java.util.List;

/**
 * Test {@code ConfigurationService}.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ConfigurationServiceTest extends TestCase {

  private static final Logger LOG =
      LogManager.getLogger(ConfigurationServiceTest.class);

  private final ConfigurationService mConfigurationService =
      ObjectInstanceService.getConfigurationService();

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  /**
   * Test whether configuration service can read property the
   * {@code configuration-index.properties} file.
   */
  public void testConfigurationLocation() {
    String daemonConfig = mConfigurationService.
        getConfigurationLocation(ConfigurationConstant.CONFIG_LOCATION_DAEMON);
    String navigationConfig = mConfigurationService.
        getConfigurationLocation(ConfigurationConstant.
                                 CONFIG_LOCATION_NAVIGATION);

    assertNotNull("Daemon configuration file not found", daemonConfig);
    assertNotNull("Navigation configuration file not found", navigationConfig);
  }

  /**
   * Test whether ITRMapping is loaded successfully or not.
   */
  public void testITRMapping() {
    final ITRMapping itrMapping = mConfigurationService.getITRMapping();
    assertNotNull(itrMapping);

    List<Company> companyList = itrMapping.getCompanies();
    assertNotNull(companyList);
    assertTrue(companyList.size() == 1);

    // Test company
    Company company = companyList.get(0);
    assertNotNull(company.getName());
    assertNotNull(company.getUrl());

    // Test company navigation tree
    ServiceNavigationTree tree = company.getServiceNavigationTree();
    assertNotNull(tree);
    assertNotNull(tree.getWelcomeMessage());

    // Test navigable tree
    NavigableTree<String, Index> navTree = tree.getNavigableTree();
    assertNotNull(navTree);
    assertTrue(navTree.getRootCount() == 1);

    NavigableTree.TreeItem<String, Index> treeItem = navTree.getRoots().get(0);
    assertNotNull(treeItem);

    LOG.debug("Key - " + treeItem.getKey());
    LOG.debug("Value - " + treeItem.getValue());
    assertNotNull(treeItem.getKey());
    assertNull(treeItem.getValue());
    assertNull(treeItem.getParentItem());

    // Test child
    List<NavigableTree.TreeItem<String, Index>> childs =
        treeItem.getChildItems();
    assertNotNull(childs);

    // Recursivly print value.
    recursivlyFindValue(treeItem);
  }

  private void recursivlyFindValue(
      final NavigableTree.TreeItem<String, Index> pTreeItem) {
    if (pTreeItem != null) {
      LOG.debug("key - " + pTreeItem.getKey());
      LOG.debug("Value - " + pTreeItem.getValue());

      for (NavigableTree.TreeItem<String, Index> item
          : pTreeItem.getChildItems()) {
        recursivlyFindValue(item);
      }
    }
  }
}

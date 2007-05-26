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
package com.ideasense.itr.test.base.navigation;

import junit.framework.TestCase;
import com.ideasense.itr.base.navigation.NavigableTree;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import java.util.Map;

/**
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class NavigableMapTest extends TestCase {

  private static final Logger LOG = LogManager.getLogger(NavigableMapTest.class);
  private NavigableTree<String, String> mNavigableTree =
      new NavigableTree<String, String>();

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    fixture();
  }

  private void fixture() {
    LOG.debug("Fixture");
    for (int i = 0; i < 10; i++) {
      final NavigableTree.TreeItem<String, String> parentItem =
          new NavigableTree.TreeItem<String, String>();
      parentItem.setKey("parent_" + i);
      parentItem.setValue("parent_value_" + i);
      for (int j = 0; j < 5; j++) {
        final String key = "key_" + i + "." +j;
        final String value = "value_" + i+j;
        final NavigableTree.TreeItem<String, String> item =
            new NavigableTree.TreeItem<String, String>();
        item.setKey(key);
        item.setValue(value);
        parentItem.addChildItem(item);

        for (int k = 0; k < 2; k++) {
          final String key2 = "key_" + i + "." +j +"." + k;
          final String value2 = "value_" + i+j+k;
          final NavigableTree.TreeItem<String, String> item2 =
              new NavigableTree.TreeItem<String, String>();
          item2.setKey(key2);
          item2.setValue(value2);
          item.addChildItem(item2);

          for (int l = 0; l < 1; l++) {
            final String key3 = "key_" + i + "." +j +"." + k + "." + l;
            final String value3 = "value_" + i+j+k+l;
            final NavigableTree.TreeItem<String, String> item3 =
                new NavigableTree.TreeItem<String, String>();
            item3.setKey(key3);
            item3.setValue(value3);
            item2.addChildItem(item3);
          }
        }
      }
      mNavigableTree.addRoot(parentItem);
    }
  }

  public void testIterator() {
    LOG.debug("Test iterator.");
    for (final NavigableTree.TreeItem<String, String> root
        : mNavigableTree.getRoots()) {
      LOG.debug("Root - " + root);
      recursivlyFindItems(root);
    }

  }

  private void recursivlyFindItems(
      final NavigableTree.TreeItem<String, String> pItem) {
    if (pItem != null) {
      final StringBuilder builder = new StringBuilder();
      builder.append("Key - ").append(pItem.getKey()).
              append(",  Value - ").append(pItem.getValue());
      LOG.debug("Child - " + builder.toString());
      for (final NavigableTree.TreeItem<String, String> item :
          pItem.getChildItems()) {
        recursivlyFindItems(item);
      }
    }
  }

  private String addTotalSpaces(final int pCount) {
    StringBuilder builder = new StringBuilder(pCount);
    for (int i = 0; i < pCount; i++) {
      builder.append("-");
    }
    return builder.toString();
  }
}

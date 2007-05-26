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
 * Keep record in hierarchical view. it supports walker to traverse all items
 * from the tree.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class NavigableTree<K, V> {

  private final List<TreeItem<K,V>> mRoots =
      new ArrayList<TreeItem<K, V>>();

  public int getRootCount() {
    return mRoots.size();
  }

  /**
   * Return internal list of root object.
   * @return list of root {@code TreeItem<K, V>} objects.
   */
  public List<TreeItem<K, V>> getRoots() {
    return mRoots;
  }

  public void addRoot(final TreeItem<K, V> pRoot) {
    mRoots.add(pRoot);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("rootCount: '").append(getRootCount()).append("',");
    builder.append("roots: '").append(mRoots).append("'");
    builder.append("}");
    return builder.toString();
  }

  /**
   * Internal object are kept through this object.
   */
  public static class TreeItem<K, V> {
    private K mKey;
    private V mValue;
    private TreeItem<K, V> mParentItem;
    private List<TreeItem<K, V>> mChildItems = new ArrayList<TreeItem<K, V>>();

    public K getKey() {
      return mKey;
    }

    public void setKey(final K pKey) {
      mKey = pKey;
    }

    public V getValue() {
      return mValue;
    }

    public void setValue(final V pValue) {
      mValue = pValue;
    }

    public TreeItem<K, V> getParentItem() {
      return mParentItem;
    }

    public void setParentItem(final TreeItem<K, V> pNextItem) {
      mParentItem = pNextItem;
    }

    public List<TreeItem<K, V>> getChildItems() {
      return mChildItems;
    }

    public void setChildItems(final List<TreeItem<K,V>> pChildItems) {
      mChildItems = pChildItems;
    }

    public void addChildItem(final TreeItem<K, V> pChildItem) {
      mChildItems.add(pChildItem);
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("{");
      builder.append("key: '").append(mKey).append("', ");
      builder.append("value: '").append(mValue).append("', ");
      builder.append("parent: '").append(mParentItem).append("', ");
      builder.append("childItems: '").append(mChildItems).append("'");
      builder.append("}");
      return builder.toString();
    }
  }
}

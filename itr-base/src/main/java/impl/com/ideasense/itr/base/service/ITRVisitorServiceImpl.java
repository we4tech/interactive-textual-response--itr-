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

import com.ideasense.itr.base.service.ITRVisitorService;
import com.ideasense.itr.base.service.NavigationService;
import com.ideasense.itr.base.navigation.*;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import java.util.List;

/**
 * Visitor service, which delegate {@code ServiceNavigationTree} to
 * the {@code ITRVisitor}.
 *
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ITRVisitorServiceImpl implements ITRVisitorService {

  private final Logger LOG = LogManager.getLogger(getClass());
  private final NavigationService mNavigationService;

  /**
   * Default constructor, {@code NavigationService} dependency is injected
   * over the constructor dependency.
   * @param pNavigationService dependency of navigation service.
   */
  public ITRVisitorServiceImpl(final NavigationService pNavigationService) {
    mNavigationService = pNavigationService;
  }

  /**
   * {@inheritDoc}<br>
   * <blockquote>
   * right now no rule is implemented to ban any visitor.
   * so it is returning {@code true}.
   * </blockquote>
   *
   * @param pVisitor {@inheritDoc}
   * @return {@inheritDoc}
   */
  public boolean isVisitorAccepted(final String pCompanyName,
                                   final ITRVisitor pVisitor) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Verify acceptance of the visitor - " + pVisitor);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   * @param pVisitor {@inheritDoc}
   */
  public void acceptVisitor(final String pCompanyName,
                            final ITRVisitor pVisitor) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Accept visitor - " + pVisitor + " for company - " +
                pCompanyName);
    }
    // Retrieve navigation service tree for the pCompanyName
    ServiceNavigationTree tree =
        mNavigationService.getServiceTree(pCompanyName);
    // recursivly browse through each element and notify visitor.
    visitNavigationTreeWithVisitor(tree, pVisitor);
  }

  private void visitNavigationTreeWithVisitor(final ServiceNavigationTree pTree,
                                              final ITRVisitor pVisitor) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Navigate tree with visitor - " + pVisitor);
    }
    // Set welcome message to visitor
    pVisitor.setWelcomeMessage(pTree.getWelcomeMessage());

    // Set error message map
    pVisitor.setTypedResponseMap(pTree.getErrorMessageMap());

    // Retrieve root tree
    NavigableTree<String, Index> navigableTree = pTree.getNavigableTree();
    if (navigableTree != null) {
      // Iterate for each roots.
      List<NavigableTree.TreeItem<String, Index>> roots =
          navigableTree.getRoots();
      for (final NavigableTree.TreeItem<String, Index> item : roots) {
        // Visit tree along visitor.
        final int level = 0;
        recursivlyVisitWithVisitor(level, pVisitor, item);
      }
    }
  }

  private void recursivlyVisitWithVisitor(
      int pLevel, final ITRVisitor pVisitor,
      final NavigableTree.TreeItem<String, Index> pItem) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Level - " + pLevel + ", item - " + pItem +
                ", visitor - " + pVisitor);
    }
    pVisitor.visit(pLevel, pItem);

    if (pVisitor.visitMore()) {
      // Verify other child items
      List<NavigableTree.TreeItem<String, Index>> childs = pItem.getChildItems();
      if (childs != null && !childs.isEmpty()) {
        for (NavigableTree.TreeItem<String, Index> child : childs) {
          recursivlyVisitWithVisitor((pLevel + 1), pVisitor, child);
        }
      }
    }
  }
}

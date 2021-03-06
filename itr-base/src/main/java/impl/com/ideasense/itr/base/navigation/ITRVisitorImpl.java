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
package impl.com.ideasense.itr.base.navigation;

import com.ideasense.itr.base.navigation.*;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import java.util.List;
import java.util.Map;

/**
 * Implementation of {@code ITRVisitor}.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ITRVisitorImpl implements ITRVisitor {

  private static final Logger LOG = LogManager.getLogger(ITRVisitorImpl.class);

  /**
   * Whitespace to display hierarchical view.
   */
  private static final String SPACE_CHAR = "  ";

  private boolean mVisitMore = true;
  private String mCommand;
  private Response mWelcomeMessage;
  private Response mResponse = new Response();
  private boolean mNullCommand = false;
  private final StringBuilder mNavigationResponseBuilder = new StringBuilder();
  private final StringBuilder mBlankStringBuilder = new StringBuilder();
  private List<String> mCommandParams;
  private String mName;
  private Map<ServiceNavigationTree.ResponseType, Response> mErrorMessageMap;
  private String mLanguageCode = "";

  /**
   * {@inheritDoc}
   * @param pLevel {@inheritDoc}
   * @param pTreeItem {@inheritDoc}
   */
  public void visit(final int pLevel,
                    final NavigableTree.TreeItem<String, Index> pTreeItem) {
    LOG.debug("Level - " + pLevel);
    if (mCommand == null || mCommand.length() == 0) {
      setResponse(mWelcomeMessage);
      mNullCommand = true;
    }
    // Match user request if not null command
    final Index index = pTreeItem.getValue();
    if (!mNullCommand && index != null
        && mCommand.equalsIgnoreCase(index.getKeyCode())) {
      setResponse(index.getResponse());
      if (LOG.isDebugEnabled()) {
        LOG.debug("Index match with '"+ mCommand +"'. generating response - " +
                  mResponse);
      }
      // Stop for iterating next item
      if (mResponse != null && mResponse.getContent() != null) {
        LOG.debug("Stopped from traversing next item.");
        mVisitMore = false;
      }
    }
    if (index != null) {
      // Generate Navigation bookmark.
      mNavigationResponseBuilder.append(generateSpaceForEach(pLevel)).
                       append(index.getKeyCode()).append(".-").
                       append(index.getTitle()).append("\r\n");
    }
  }

  private String generateSpaceForEach(final int pLevel) {
    // Clear string builder
    mBlankStringBuilder.replace(0, mBlankStringBuilder.length(), "");
    for (int i = 0; i < pLevel; i++) {
      mBlankStringBuilder.append(SPACE_CHAR);
    }
    return mBlankStringBuilder.toString();
  }

  /**
   * {@inheritDoc}
   * @return {@inheritDoc}
   */
  public boolean visitMore() {
    return mVisitMore;
  }

  /**
   * {@inheritDoc}
   * @return {@inheritDoc}
   */
  public String getCommand() {
    return mCommand;
  }

  /**
   * {@inheritDoc}
   * @param pCommand {@inheritDoc}
   */
  public void setCommand(final String pCommand) {
    mCommand = pCommand;
  }

  /**
   * {@inheritDoc}
   * @param pCommandParams {@inheritDoc}
   */
  public void setCommadParams(final List<String> pCommandParams) {
    mCommandParams = pCommandParams;
  }

  /**
   * {@inheritDoc}
   * @return {@inheritDoc}
   */
  public List<String> getCommandParams() {
    return mCommandParams;
  }

  /**
   * {@inheritDoc}
   * @param pWelcomeMessage {@inheritDoc}
   */
  public void setWelcomeMessage(final Response pWelcomeMessage) {
    mWelcomeMessage = pWelcomeMessage;
  }

  /**
   * {@inheritDoc}
   * @return {@inheritDoc}
   */
  public Response getWelcomeMessage() {
    return mWelcomeMessage;
  }

  /**
   * {@inheritDoc}
   * @return {@inheritDoc}
   */
  public Response getResponse() {
    // Already found response, so we don't need to set the service 
    // bookmark response.
    if (!mVisitMore) {
      return mResponse;
    } else {
      mResponse.setNavigation(mNavigationResponseBuilder.toString());
      return mResponse;
    }
  }

  /**
   * {@inheritDoc}
   * @param pName {@inheritDoc}
   */
  public void setName(final String pName) {
    mName = pName;
  }

  public String getName() {
    return mName;
  }

  public void setTypedResponseMap(
      final Map<ServiceNavigationTree.ResponseType, Response> pMap) {
    mErrorMessageMap = pMap;
  }

  public Response getTypedResponse(
      final ServiceNavigationTree.ResponseType pResponseType) {
    return mErrorMessageMap.get(pResponseType);
  }

  public void setResponse(final Response pResponse) {
    pResponse.setLanguageCode(mLanguageCode);
    mResponse = pResponse;
  }

  public void setVisitMore(final boolean pState) {
    mVisitMore = false;
  }

  public void setLanguageCode(final String pLangCode) {
    mLanguageCode = pLangCode;
  }
}

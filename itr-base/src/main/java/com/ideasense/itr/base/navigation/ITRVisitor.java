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

import java.util.List;
import java.util.Map;

/**
 * If Visitor is accepted, {@code visit} method will be invoked
 * with navigation {@code Index} and level information.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public interface ITRVisitor {

  /**
   * Visit {@code TreeItem} object which {@code level} is mentioned over the
   * {@code pLevel}.
   * @param pLevel object level number.
   * @param pTreeItem visitable object.
   */
  public void visit(final int pLevel,
                    final NavigableTree.TreeItem<String, Index>  pTreeItem);

  /**
   * Return true if visitor wants to visit more. otherwise visit will be
   * halted on that state.
   * @return true if visitor wants to visit more.
   */
  public boolean visitMore();

  /**
   * User written command here.
   * @return user written command.
   */
  public String getCommand();

  /**
   * Set User written command
   * @param pCommand user written command
   */
  public void setCommand(final String pCommand);

  /**
   * Extra parameters are determined by the user chat message. <br>
   * first word is counted as command and the rest of the commands are
   * declared as params. <br>
   * mostly useful for plugin.
   * @param pCommandParams set parameters.
   */
  public void setCommadParams(final List<String> pCommandParams);

  /**
   * Return list of command parameters.
   * @return list of command parameters.
   */
  public List<String> getCommandParams();

  /**
   * If no command found {@code ITRVisitor} will use this response to send
   * as welcome message.
   * @param pWelcomeMessage welcome response.
   */
  public void setWelcomeMessage(final Response pWelcomeMessage);

  /**
   * Return the instance of welcome response.
   * @return welcome respponse.
   */
  public Response getWelcomeMessage();

  /**
   * Prepare the object which will be thrown to the ITR client.
   * @return the response which will be thrown to the ITR client.
   */
  public Response getResponse();

  /**
   * ITR visitor name.
   * @param pName ITR visitor name.
   */
  public void setName(final String pName);

  /**
   * Return ITR visitor name.
   * @return ITR visitor name.
   */
  public String getName();

  /**
   * Set error message map.
   * @param pMap error message map.
   */
  public void setTypedResponseMap(
      final Map<ServiceNavigationTree.ResponseType, Response> pMap);

  /**
   * Return error message based on it's type.
   * @param pResponseType error message type.
   * @return error message.
   */
  public Response getTypedResponse(
      final ServiceNavigationTree.ResponseType pResponseType);

  /**
   * Set response, it useful when response is injected from other service.
   * @param pResponse response content in {@code Response} class.
   */
  public void setResponse(final Response pResponse);

  /**
   * Set flag for visit more state. if false set it won't iterate through
   * objects structure.
   * @param pState true/false value is accepted.
   */
  void setVisitMore(final boolean pState);

  /**
   * Langauge code.
   * @param pLangCode language code.
   */
  void setLanguageCode(final String pLangCode);
}
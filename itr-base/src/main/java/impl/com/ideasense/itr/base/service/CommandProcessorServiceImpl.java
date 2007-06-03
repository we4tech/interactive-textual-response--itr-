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

import com.ideasense.itr.base.service.CommandProcessorService;
import com.ideasense.itr.base.service.ObjectInstanceService;
import com.ideasense.itr.base.service.ITRVisitorService;
import com.ideasense.itr.base.navigation.ITRVisitor;
import com.ideasense.itr.base.navigation.ServiceNavigationTree;
import static com.ideasense.itr.base.navigation.ServiceNavigationTree.*;
import com.ideasense.itr.common.configuration.ProtocolConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import java.util.*;

/**
 * Implementation of {@code CommandProcessService}.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class CommandProcessorServiceImpl implements CommandProcessorService {

  private static final String COMMAND_HELP = "help";
  private static final String COMMAND_SYSTEM_PREFIX = "sys:";
  private static final String SYSTEM_COMMAND_SEPARATOR = ":";
  private static final String COMMAND_LANG = "lang";

  private final Logger LOG = LogManager.getLogger(getClass());
  private final boolean DEBUG = LOG.isDebugEnabled();
  private final Map<String, String> mCacheLanguagePref =
      new HashMap<String, String>();

  /**
   * {@inheritDoc}
   * @param pProtocolConfiguration {@inheritDoc}
   * @param pName {@inheritDoc}
   * @param pCommand {@inheritDoc}
   * @return {@inheritDoc}
   */
  public ITRVisitor processCommand(
      final ProtocolConfiguration pProtocolConfiguration,
      final String pName, final String pCommand) {
    if (DEBUG) {
      LOG.debug("Process command - " + pName + ", " + pCommand);
    }
    if (pName == null) {
      throw new IllegalArgumentException("Null value is not accecpted for " +
                                         "user name.");
    } else {
      final String company = pProtocolConfiguration.getCompany();
      final ITRVisitor visitor = newVisitor(pName, pCommand);
      final ITRVisitorService visitorService =
            ObjectInstanceService.getVisitorService();
      // Set language preference.
      setLanguagePref(visitor);

      // Detect default commands
      if (COMMAND_HELP.equalsIgnoreCase(pCommand)) {
        LOG.debug("Help command detected.");
        // Help command
        visitorService.acceptVisitor(company, visitor);
        visitor.setVisitMore(false);
        visitor.setResponse(visitor.
            getTypedResponse(ResponseType.HELP_COMMAND));
        return visitor;
      } else if (pCommand != null && pCommand.startsWith(COMMAND_SYSTEM_PREFIX))
      {
        // Detect system command
        visitorService.acceptVisitor(company, visitor);
        visitor.setVisitMore(false);
        if (handleSystemCommand(pCommand, visitor)) {
          visitor.setResponse(visitor.getTypedResponse(
              ResponseType.SUCCESSFUL_SYSTEM_RESPONSE));
        } else {
          visitor.setResponse(visitor.getTypedResponse(
              ResponseType.FAILED_SYSTEM_RESPONSE));
        }
        return visitor;
      } else {
        LOG.debug("General command detected.");
        // Normal command
        if (visitorService.
            isVisitorAccepted(pProtocolConfiguration.getCompany(), visitor)) {
          // Accepte user request now let her navigate the service.
          visitorService.acceptVisitor(company, visitor);
          if (DEBUG) {
            LOG.debug("Visitor accepted - " + visitor);
          }
          return visitor;
        } else {
          if (DEBUG) {
            LOG.debug("Visitor denied - " + visitor);
          }
          return null;
        }
      }
    }
  }

  private void setLanguagePref(final ITRVisitor pVisitor) {
    final String langCode = mCacheLanguagePref.get(pVisitor.getName());
    if (langCode != null) {
      LOG.debug("ReturnAfterOtherSettings - " + langCode);
      pVisitor.setLanguageCode(langCode);
    }
  }

  private boolean handleSystemCommand(final String pCommand,
                                      final ITRVisitor pVisitor) {
    LOG.debug("Handle system command.");
    try {
      final String[] split = pCommand.split(SYSTEM_COMMAND_SEPARATOR);
      final String command = split[1];
      if (COMMAND_LANG.equalsIgnoreCase(command)) {
        final String langCode = split[2];
        // Update cache table
        mCacheLanguagePref.put(pVisitor.getName(), langCode);
        return true;
      }
    } catch (Exception e) {
      // Invalid user request, it is not required to handle such exception.
      // rather return false, which means invalid command.
      LOG.debug("Valid to handle system command", e);
    }
    return false;
  }

  private ITRVisitor newVisitor(final String pName, final String pCommand) {
    ITRVisitor visitor = ObjectInstanceService.newVisitor();
    visitor.setName(pName);
    // Parse message content to command and paramars
    // first string token is treated as command and rest of the tokens
    // are treated as comm params.
    prepareCommandAndParams(pCommand, visitor);
    
    return visitor;
  }

  /**
   * First string token is treated as command and rest of tokens are treated
   * as parameters.
   * @param pCommand instance message object.
   * @param pVisitor messenger client who is visiting the navigation tree.
   */
  private void prepareCommandAndParams(final String pCommand,
                                       final ITRVisitor pVisitor) {
    if (DEBUG) {
      LOG.debug("Prepare command and params from - " + pCommand);
    }
    if (pCommand != null) {
      final String messageContent = pCommand;
      if (messageContent != null) {
        final StringTokenizer stringTokenizer =
            new StringTokenizer(messageContent);
        // Set command
        if (stringTokenizer.hasMoreTokens()) {
          pVisitor.setCommand(stringTokenizer.nextToken());
        }
        // Set parameters
        final List<String> commandParams = new ArrayList<String>();
        while (stringTokenizer.hasMoreTokens()) {
          commandParams.add(stringTokenizer.nextToken());
        }
        pVisitor.setCommadParams(commandParams);
      } else {
        LOG.debug("Instance Message content is empty.");
      }
    }
  }
}

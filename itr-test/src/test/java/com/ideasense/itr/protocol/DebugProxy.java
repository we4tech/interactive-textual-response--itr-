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
package com.ideasense.itr.protocol;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * This proxy send out the debug message during the invocation of methods.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class DebugProxy implements InvocationHandler {

  /**
   * Register logger for this class.
   */
  private static final Logger LOG = LogManager.getLogger(DebugProxy.class);
  private final boolean DEBUG = LOG.isDebugEnabled();

  private final Object mObjectInstance;

  /**
   * Default constructor.
   * @param pObjectInstance object instance.
   */
  public DebugProxy(final Object pObjectInstance) {
    mObjectInstance = pObjectInstance;
  }

  /**
   * {@inheritDoc}
   * <br>
   * Send out debug message.
   *
   * @param pRoxy {@inheritDoc}
   * @param pMethod {@inheritDoc}
   * @param pArgs {@inheritDoc}
   * @return {@inheritDoc}
   * @throws Throwable {@inheritDoc}
   */
  public Object invoke(final Object pRoxy, final Method pMethod,
                       final Object[] pArgs) throws Throwable {
    if (DEBUG) {
      LOG.debug("Invoking before method - " + pMethod.getName());
    }
    Object result = null;
    try {
      result = pMethod.invoke(mObjectInstance, pArgs);
    } catch (Exception e) {
      throw new RuntimeException("Failed to invoke", e);
    } finally {
      if (DEBUG) {
        LOG.debug("Invoking after method - " + pMethod.getName());
      }
    }
    return result;
  }
}

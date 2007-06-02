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
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.HashMap;

import net.sf.jml.MsnSwitchboard;

/**
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class MockBuilder<T> implements InvocationHandler {

  private static final Logger LOG = LogManager.getLogger(MockBuilder.class);
  private final Map<String, Logic<? extends Object>> mLogicMap =
      new HashMap<String, Logic<? extends Object>>();
  private final Class<MsnSwitchboard> mInterface;

  public MockBuilder(final Class<MsnSwitchboard> pInterface) {
    mInterface = pInterface;
  }

  public void registerLogic(final String pMethodName, final Logic<? extends Object> pLogic) {
    mLogicMap.put(pMethodName, pLogic);
  }

  public Object invoke(final Object pRoxy, final Method pMethod,
                       final Object[] pArgs) throws Throwable {
    final Logic<? extends Object> logic = mLogicMap.get(pMethod.getName());

    if (logic == null) {
      throw new RuntimeException("Method - " + pMethod.getName() + " has no " +
                                 "defined logic.");
    }
    // Execute logic
    return logic.execute(pArgs);
  }

  public T proxy() {
    return (T) Proxy.newProxyInstance(mInterface.getClassLoader(),
                         new Class[] {mInterface}, this);
  }

  public static interface Logic<T> {
    public T execute(final Object[] pArgs);
  }
}

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
package com.ideasense.itr.common.helper;

import java.io.InputStream;

/**
 * Resource discovery related functionalities are covered under this class.
 * @author <a href="mailto:hasan@somewherein.net">nhm tanveer hossain khan (hasan)</a>
 */
public class ResourceLocator {

  /**
   * Return {@code InputStream} from current class loader or lookup system default
   * class loader.
   * @param pFileName file name.
   * @return the stream of the file.
   */
  public static InputStream getInputStream(final String pFileName) {
    InputStream inputStream = ResourceLocator.class.getClassLoader().
        getResourceAsStream(pFileName);
    if (inputStream == null) {
      inputStream = ClassLoader.getSystemResourceAsStream(pFileName);
    }
    if (inputStream == null) {
      throw new RuntimeException("File - " + pFileName + ", not found.");
    }
    return inputStream;
  }
}

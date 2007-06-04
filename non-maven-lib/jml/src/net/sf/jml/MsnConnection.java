/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jml;

/**
 * Msn connection information.
 * 
 * @author Roger Chen
 */
public interface MsnConnection {

    /**
     * Get the remote ip.
     * 
     * @return
     * 		remote ip
     */
    public String getRemoteIP();

    /**
     * Get remote port.
     * 
     * @return
     * 		remote port
     */
    public int getRemotePort();

    /**
     * Get ip internal.
     * 
     * @return
     * 		ip internal
     */
    public String getInternalIP();

    /**
     * Get port internal.
     * 
     * @return
     * 		port internal
     */
    public int getInternalPort();

    /**
     * Get ip external. If don't known, return null.
     * 
     * @return
     * 		ip external
     */
    public String getExternalIP();

    /**
     * Get port external. If don't known, return -1.
     * 
     * @return
     * 		port external
     */
    public int getExternalPort();

    /**
     * Get connection type, if don't known, return null.
     * 
     * @return
     * 		connection type
     */
    public MsnConnectionType getConnectionType();
}
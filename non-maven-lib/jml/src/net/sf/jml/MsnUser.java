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
 * MSN User.
 * 
 * @author Roger Chen
 */
public interface MsnUser {

    /**
     * Email, can't be changed.
     * 
     * @return
     * 		email
     */
    public Email getEmail();

    /**
     * Display name, can be changed by user self.
     * 
     * @return
     * 		display name 
     */
    public String getDisplayName();

    /**
     * User status, can be changed by user self.
     * 
     * @return
     * 		user status
     */
    public MsnUserStatus getStatus();

    /**
     * Client id, can be changed by user self.
     *
     * @return
     * 		client id 
     */
    public MsnClientId getClientId();

    /**
     * User properties, can be changed by user self.
     * 
     * @return
     * 		user properties
     */
    public MsnUserProperties getProperties();

    /**
     * When user changed his name, this is useful for compare.
     * 
     * @return
     * 		old display name
     */
    public String getOldDisplayName();

    /**
     * When user changed his status, this is useful for compare.
     * 
     * @return
     * 		old display status
     */
    public MsnUserStatus getOldStatus();

}
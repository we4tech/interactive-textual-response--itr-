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
package net.sf.jml.event;

import net.sf.jml.MsnMessenger;

/**
 * Msn messenger listener.
 * 
 * @author Roger Chen
 */
public interface MsnMessengerListener {

    /**
     * Login completed.
     * 
     * @param messenger
     * 		the MsnMessenger who login
     */
    public void loginCompleted(MsnMessenger messenger);

    /**
     * Logout. Even not login completed may logout.
     * 
     * @param messenger
     * 		MsnMessenger
     */
    public void logout(MsnMessenger messenger);

    /**
     * Caught exception.
     * 
     * @param messenger
     * 		MsnMessenger
     * @param throwable
     *      cause
     */
    public void exceptionCaught(MsnMessenger messenger, Throwable throwable);

}
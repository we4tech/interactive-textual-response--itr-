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
package net.sf.jml.impl;

import net.sf.jml.Email;
import net.sf.jml.MsnMessenger;

/**
 * MsnMessenger factory.
 * 
 * @author Roger Chen
 */
public class MsnMessengerFactory {

    /**
     * Create an instance of MsnMessenger.
     * 
     * @param email
     * 		email
     * @param password
     * 		password
     * @return
     * 		MsnMessenger instance
     * @throws IllegalArgumentException
     */
    public static MsnMessenger createMsnMessenger(String email, String password)
            throws IllegalArgumentException {
        Email emailAddr = Email.parseStr(email);
        if (emailAddr == null)
            throw new IllegalArgumentException("email is not available");
        return new SimpleMessenger(emailAddr, password);
    }
}
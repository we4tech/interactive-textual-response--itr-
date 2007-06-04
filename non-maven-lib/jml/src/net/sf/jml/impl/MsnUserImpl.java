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
import net.sf.jml.MsnClientId;
import net.sf.jml.MsnUser;
import net.sf.jml.MsnUserProperties;
import net.sf.jml.MsnUserStatus;
import net.sf.jml.Telephone;

/** 
 * @author Roger Chen
 */
public abstract class MsnUserImpl implements MsnUser {

    private Email email;
    private Telephone telephone;

    private String displayName;
    private MsnUserStatus status = MsnUserStatus.OFFLINE;

    private String oldDisplayName;
    private MsnUserStatus oldStatus = status;

    private MsnClientId clientId = MsnClientId.parseInt(0);
    private final MsnUserProperties properties = new MsnUserPropertiesImpl();

    public Email getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public MsnUserStatus getStatus() {
        return status;
    }

    public String getOldDisplayName() {
        return oldDisplayName;
    }

    public MsnUserStatus getOldStatus() {
        return oldStatus;
    }

    public MsnClientId getClientId() {
        return clientId;
    }

    public MsnUserProperties getProperties() {
        return properties;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setDisplayName(String displayName) {
        if (displayName != null) {
            if (this.displayName == null)
                oldDisplayName = displayName;
            else
                oldDisplayName = this.displayName;
            this.displayName = displayName;
        }
    }

    public void setStatus(MsnUserStatus status) {
        if (status != null) {
            oldStatus = this.status;
            this.status = status;
        }
    }

    public void setClientId(MsnClientId clientId) {
        this.clientId = clientId;
    }

	public Telephone getTelephone() {
		return telephone;
	}

	public void setTelephone(Telephone telephone) {
		this.telephone = telephone;
	}

}
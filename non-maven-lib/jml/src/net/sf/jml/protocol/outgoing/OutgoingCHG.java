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
package net.sf.jml.protocol.outgoing;

import net.sf.jml.MsnClientId;
import net.sf.jml.MsnObject;
import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnUserStatus;
import net.sf.jml.protocol.MsnOutgoingMessage;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * Change user status.
 * <p>
 * Supported Protocol: All
 * <p>
 * Syntax: CHG trId userStatus clientId
 * 
 * @author Roger Chen
 */
public class OutgoingCHG extends MsnOutgoingMessage {

	 private boolean firstSend;
	    private MsnObject msnObj;

	    public OutgoingCHG(MsnProtocol protocol) {
	        super(protocol);
	        setCommand("CHG");
	    }

	    public boolean isFirstSend() {
	        return firstSend;
	    }

	    public MsnObject getMsnObj() {
	        return msnObj;
	    }

	    /**
	     * When first send, will received some init status messages.
	     *
	     * @param firstSend
	     * 		is first send
	     */
	    public void setFirstSend(boolean firstSend) {
	        this.firstSend = firstSend;
	    }

	    public void setStatus(MsnUserStatus status) {
	        if (status == null) {
	            throw new NullPointerException();
	        }
	        if (status == MsnUserStatus.OFFLINE) {
	            throw new IllegalArgumentException(status.toString());
	        }
	        setParam(0, status.getStatus());
	    }

	    public void setDisplayPicture(MsnObject msnObj) {
	        if (msnObj == null) {
	            return;
	        }
	        setParam(2, StringUtils.urlEncode(msnObj.toString()));
	    }


	    public void setClientId(MsnClientId clientId) {
	        setParam(1, String.valueOf(clientId.getId()));
	    }

	    public MsnUserStatus getStatus() {
	        return MsnUserStatus.parseStr(getParam(0));
	    }

	    public MsnClientId getClientId() {
	        return MsnClientId.parseInt(NumberUtils.stringToInt(getParam(1)));
	    }
}
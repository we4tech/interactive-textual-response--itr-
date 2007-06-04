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

import net.sf.jml.*;
import net.sf.jml.MsnObject;
import net.sf.jml.protocol.outgoing.*;
import net.sf.jml.util.StringUtils;
import net.sf.jml.util.GUID;
import net.sf.jml.message.p2p.DisplayPictureDuelManager;

/**
 * @author Roger Chen
 */
public class MsnOwnerImpl extends MsnUserImpl implements MsnOwner {

	private final MsnMessenger messenger;

	private final String password;

	private boolean verified;

	private boolean onlyNotifyAllowList;

	private boolean notifyMeWhenSomeoneAddedMe;

	private MsnUserStatus initStatus = MsnUserStatus.ONLINE;

	private MsnObject displayPicture;

	public MsnOwnerImpl(MsnMessenger messenger, Email email, String password) {
		this.messenger = messenger;
		this.password = password;
		setEmail(email);
	}

	public MsnMessenger getMessenger() {
		return messenger;
	}

	public boolean isVerified() {
		return verified;
	}

	public MsnUserStatus getInitStatus() {
		return initStatus;
	}

	public void setInitStatus(MsnUserStatus initStatus) {
		if (initStatus == MsnUserStatus.OFFLINE) {
			throw new IllegalArgumentException(initStatus.toString());
		}
		this.initStatus = initStatus;
	}

	public boolean isOnlyNotifyAllowList() {
		return onlyNotifyAllowList;
	}

	public boolean isNotifyMeWhenSomeoneAddedMe() {
		return notifyMeWhenSomeoneAddedMe;
	}

	public void setDisplayName(String displayName) {
		if (displayName != null) {
            if (displayName.equals(getDisplayName())) {
                return;
            }
            MsnProtocol protocol = messenger.getActualMsnProtocol();
			// In MSNP10 use PRP MFN, other use REA
			if (protocol.after(MsnProtocol.MSNP9)) {
				setProperty(MsnUserPropertyType.MFN, StringUtils
						.urlEncode(displayName));
			} else {
				OutgoingREA outgoing = new OutgoingREA(protocol);
				outgoing.setId(getEmail().getEmailAddress());
				outgoing.setFriendlyName(displayName);
				messenger.send(outgoing);
			}
		}
	}

	public void setClientId(MsnClientId clientId) {
		if (clientId != null && !clientId.equals(getClientId())) {
			OutgoingCHG outgoing = new OutgoingCHG(messenger
					.getActualMsnProtocol());
			outgoing.setClientId(clientId);
			outgoing.setStatus(getStatus());
			outgoing.setDisplayPicture(getDisplayPicture());
			messenger.send(outgoing);
		}
	}

	public void setStatus(MsnUserStatus status) {
		if (status != null && status != MsnUserStatus.OFFLINE
				&& status != getStatus()) {
			OutgoingCHG outgoing = new OutgoingCHG(messenger.getActualMsnProtocol());
			outgoing.setClientId(getClientId());
			outgoing.setStatus(status);
			outgoing.setDisplayPicture(getDisplayPicture());
			messenger.send(outgoing);
		}
	}

	public void setPersonalMessage(String personalMessage) {
        OutgoingUUX message = new OutgoingUUX(messenger.getActualMsnProtocol());
        message.setPersonalMessage(personalMessage);
        messenger.send(message);
	}

    public void setCurrentMedia(String currentMedia) {
        OutgoingUUX message = new OutgoingUUX(messenger.getActualMsnProtocol());
        message.setCurrentMedia(currentMedia);
        messenger.send(message);
    }

    public void setCurrentMedia(String title, String artist, String album, GUID contentId) {
        OutgoingUUX message = new OutgoingUUX(messenger.getActualMsnProtocol());
        message.setCurrentMedia(title, artist, album, contentId);
        messenger.send(message);
	}

    public void setPersonalMessageAndCurrentMedia(String personalMessage, String currentMedia) {
        OutgoingUUX message = new OutgoingUUX(messenger.getActualMsnProtocol());
        message.setPersonalMessage(personalMessage);
        message.setCurrentMedia(currentMedia);
        messenger.send(message);
    }

    public void setPersonalMessageAndCurrentMedia(String personalMessage, String title, String artist, String album, GUID contentId) {
        OutgoingUUX message = new OutgoingUUX(messenger.getActualMsnProtocol());
        message.setPersonalMessage(personalMessage);
        message.setCurrentMedia(title, artist, album, contentId);
        messenger.send(message);
    }

    public void setDisplayPicture(MsnObject displayPicture) {
		if (displayPicture != null
				&& !displayPicture.equals(this.displayPicture)) {
			setInitDisplayPicture(displayPicture);
			OutgoingCHG outgoing = new OutgoingCHG(messenger
					.getActualMsnProtocol());
			outgoing.setClientId(getClientId());
			outgoing.setStatus(getStatus());
			outgoing.setDisplayPicture(getDisplayPicture());
			messenger.send(outgoing);
		}
	}

	public void setProperty(MsnUserPropertyType type, String value) {
		if (type != null) {
			OutgoingPRP outgoing = new OutgoingPRP(messenger
					.getActualMsnProtocol());
			outgoing.setPropertyType(type);
			outgoing.setProperty(value);
			messenger.send(outgoing);
		}
	}

	public void setOnlyNotifyAllowList(boolean b) {
		if (b != isOnlyNotifyAllowList()) {
			OutgoingBLP outgoing = new OutgoingBLP(messenger
					.getActualMsnProtocol());
			outgoing.setOnlyNotifyAllowList(b);
			messenger.send(outgoing);
		}
	}

	public void setNotifyMeWhenSomeoneAddedMe(boolean b) {
		if (b != isNotifyMeWhenSomeoneAddedMe()) {
			OutgoingGTC outgoing = new OutgoingGTC(messenger
					.getActualMsnProtocol());
			outgoing.setNotifyMeWhenSomeoneAddedMe(b);
			messenger.send(outgoing);
		}
	}

	public String toString() {
		return "MsnOwner: [Email] " + getEmail() + " [DisplayName] "
				+ getDisplayName() + " [Status] " + getStatus();
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public void setInitDisplayPicture(MsnObject displayPicture) {
		this.displayPicture = displayPicture;
		if (displayPicture != null) {
			DisplayPictureDuelManager.getDuelManager().setDisplayPicutre(
					displayPicture);
		}
	}

	public String getPassword() {
		return password;
    }

	public MsnObject getDisplayPicture() {
		return displayPicture;
	}

	public void fSetDisplayName(String displayName) {
		super.setDisplayName(displayName);
	}

	public void fSetClientId(MsnClientId clientId) {
		super.setClientId(clientId);
	}

	public void fSetStatusF(MsnUserStatus status) {
		super.setStatus(status);
	}

	public void fSetNotifyMeWhenSomeoneAddedMe(boolean b) {
		notifyMeWhenSomeoneAddedMe = b;
	}

	public void fSetOnlyNotifyAllowList(boolean b) {
		onlyNotifyAllowList = b;
	}   
}
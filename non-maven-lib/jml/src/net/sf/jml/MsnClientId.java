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
 * Client Identification, see 
 * <a href="http://www.hypothetic.org/docs/msn/notification/presence.php">http://www.hypothetic.org/docs/msn/notification/presence.php</a>
 * "Client Identification numbers" section,
 * <a href="http://siebe.bot2k3.net/docs/?url=clientid.html">http://siebe.bot2k3.net/docs/?url=clientid.html</a> and
 * <a href="http://msnblog.stuffplug.com/?p=116">http://msnblog.stuffplug.com/?p=116</a>
 * 
 * @author Roger Chen
 */
public final class MsnClientId {

    private int id;

    private static final int RUNNING_ON_MOBILE_DEVICE = 1 << 0;
    private static final int SUPPORT_GIF_INK_MESSAGE = 1 << 2;
    private static final int SUPPORT_ISF_INK_MESSAGE = 1 << 3;
    private static final int SUPPORT_VIDEO_CONVERSATION = 1 << 4;
    private static final int SUPPORT_MULTI_PACKET_MESSAGE = 1 << 5;
    private static final int RUNNING_ON_MSN_MOBILE_DEVICE = 1 << 6;
    private static final int RUNNING_ON_MSN_DIRECT_DEVICE = 1 << 7;
    private static final int RUNNING_ON_WEB = 1 << 9;
    private static final int SUPPORT_DIRECT_IM = 1 << 14;
    private static final int SUPPORT_WINKS = 1 << 15;
    private static final int SUPPORT_MSN_SEARCH = 1 << 16;
    private static final int SUPPORT_RECEIVE_VOICE_CLIP = 1 << 18;

    private static final int SUPPORT_PROTOCOL = 1 << 28;

    public static final int SUPPORT_UP_TO_MSNC0 = 0;
    public static final int SUPPORT_UP_TO_MSNC1 = 1;
    public static final int SUPPORT_UP_TO_MSNC2 = 2;
    public static final int SUPPORT_UP_TO_MSNC3 = 3;
    public static final int SUPPORT_UP_TO_MSNC4 = 4;
    public static final int SUPPORT_UP_TO_MSNC5 = 5;

    public static MsnClientId getDefaultSupportedClientId(MsnProtocol protocol) {
        MsnClientId clientId = new MsnClientId(0);
        clientId.setRunningOnMobileDevice(false);
        clientId.setSupportGifInkMessage(true);
        clientId.setSupportIsfInkMessage(true);
        clientId.setSupportVideoConversation(false);
        clientId.setSupportMultiPacketMessage(true);
        clientId.setRunningOnMsnMobileDevice(false);
        clientId.setRunningOnMsnDirectDevice(false);
        clientId.setRunningOnWeb(false);

        clientId.setSupportDirectIM(false);
        clientId.setSupportMsnSearch(false);
        if (protocol == MsnProtocol.MSNP8)
            clientId.setSupportedClientProtocol(SUPPORT_UP_TO_MSNC0);
        else if (protocol == MsnProtocol.MSNP9)
            clientId.setSupportedClientProtocol(SUPPORT_UP_TO_MSNC1);
        else if (protocol == MsnProtocol.MSNP10)
            clientId.setSupportedClientProtocol(SUPPORT_UP_TO_MSNC1);
        else if (protocol.after(MsnProtocol.MSNP10)) {
            clientId.setSupportedClientProtocol(SUPPORT_UP_TO_MSNC5);
            clientId.setSupportWinks(true);
            clientId.setSupportReceiveVoiceClip(true);
        }
        return clientId;
    }

    public static MsnClientId parseInt(int id) {
        return new MsnClientId(id);
    }

    private MsnClientId(int id) {
        setId(id);
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    private boolean isSupportSomeFlag(int flag) {
        return (id & flag) != 0;
    }

    private void setSupportSomeFlag(int flag, boolean support) {
        if (support) {
            id = id | flag;
        } else {
            id = id & (~flag);
        }
    }

    /**
     * See <a href="http://www.hypothetic.org/docs/msn/general/overview.php">http://www.hypothetic.org/docs/msn/general/overview.php</a>
     * "What is the MSN Client protocol" Section
     * 
     * @return
     * 		get supported client protocol
     */
    public int getSupportedClientProtocol() {
        return id / SUPPORT_PROTOCOL;
    }

    private void setSupportedClientProtocol(int protocol) {
        id = id % SUPPORT_PROTOCOL + SUPPORT_PROTOCOL * protocol;
    }

    public boolean isRunningOnMobileDevice() {
        return isSupportSomeFlag(RUNNING_ON_MOBILE_DEVICE);
    }

    private void setRunningOnMobileDevice(boolean b) {
        setSupportSomeFlag(RUNNING_ON_MOBILE_DEVICE, b);
    }

    public boolean isSupportGifInkMessage() {
        return isSupportSomeFlag(SUPPORT_GIF_INK_MESSAGE);
    }

    private void setSupportGifInkMessage(boolean b) {
        setSupportSomeFlag(SUPPORT_GIF_INK_MESSAGE, b);
    }

    public boolean isSupportIsfInkMessage() {
        return isSupportSomeFlag(SUPPORT_ISF_INK_MESSAGE);
    }

    private void setSupportIsfInkMessage(boolean b) {
        setSupportSomeFlag(SUPPORT_ISF_INK_MESSAGE, b);
    }

    public boolean isSupportVideoConversation() {
        return isSupportSomeFlag(SUPPORT_VIDEO_CONVERSATION);
    }

    private void setSupportVideoConversation(boolean b) {
        setSupportSomeFlag(SUPPORT_VIDEO_CONVERSATION, b);
    }

    public boolean isSupportMultiPacketMessage() {
        return isSupportSomeFlag(SUPPORT_MULTI_PACKET_MESSAGE);
    }

    private void setSupportMultiPacketMessage(boolean b) {
        setSupportSomeFlag(SUPPORT_MULTI_PACKET_MESSAGE, b);
    }

    public boolean getRunningOnMsnMobileDevice() {
        return isSupportSomeFlag(RUNNING_ON_MSN_MOBILE_DEVICE);
    }

    private void setRunningOnMsnMobileDevice(boolean b) {
        setSupportSomeFlag(RUNNING_ON_MSN_MOBILE_DEVICE, b);
    }

    public boolean getRunningOnMsnDirectDevice() {
        return isSupportSomeFlag(RUNNING_ON_MSN_DIRECT_DEVICE);
    }

    private void setRunningOnMsnDirectDevice(boolean b) {
        setSupportSomeFlag(RUNNING_ON_MSN_DIRECT_DEVICE, b);
    }

    public boolean isRunningOnWeb() {
        return isSupportSomeFlag(RUNNING_ON_WEB);
    }

    private void setRunningOnWeb(boolean b) {
        setSupportSomeFlag(RUNNING_ON_WEB, b);
    }

    public boolean isSupportDirectIM() {
        return isSupportSomeFlag(SUPPORT_DIRECT_IM);
    }

    private void setSupportDirectIM(boolean b) {
        setSupportSomeFlag(SUPPORT_DIRECT_IM, b);
    }

    public boolean isSupportWinks() {
        return isSupportSomeFlag(SUPPORT_WINKS);
    }

    private void setSupportWinks(boolean b) {
        setSupportSomeFlag(SUPPORT_WINKS, b);
    }

    public boolean isSupportMsnSearch() {
        return isSupportSomeFlag(SUPPORT_MSN_SEARCH);
    }

    private void setSupportMsnSearch(boolean b) {
        setSupportSomeFlag(SUPPORT_MSN_SEARCH, b);
    }

    public boolean isSupportReceiveVoiceClip() {
        return isSupportSomeFlag(SUPPORT_RECEIVE_VOICE_CLIP);
    }

    private void setSupportReceiveVoiceClip(boolean b) {
        setSupportSomeFlag(SUPPORT_RECEIVE_VOICE_CLIP, b);
    }

    public String toString() {
        return String.valueOf(id);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MsnClientId)) {
            return false;
        }
        MsnClientId clientId = (MsnClientId) obj;
        return clientId.id == id;
    }

    public int hashCode() {
        return 31 * id;
    }
}

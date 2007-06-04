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
package net.sf.jml.message;

import net.sf.jml.util.JmlConstants;

/**
 * Message constants.
 *
 * Ses: <a href="http://www.hypothetic.org/docs/msn/notification/messages.php">http://www.hypothetic.org/docs/msn/notification/messages.php</a>
 * 
 * @author Roger Chen
 */
public final class MessageConstants {

    public static final String CT_TEXT = "text/plain";

    public static final String CT_CONTROL = "text/x-msmsgscontrol";

    public static final String CT_PROFILE = "text/x-msmsgsprofile";

    public static final String CT_INIT_EMAIL_NOTIFY = "text/x-msmsgsinitialemailnotification";

    public static final String CT_INIT_MAIL_DATA_NOTIFY = "text/x-msmsgsinitialmdatanotification";

    public static final String CT_REALTIME_EMAIL_NOTIFY = "text/x-msmsgsemailnotification";

    public static final String CT_ACTIVE_EMAIL_NOTIFY = "text/x-msmsgsactivemailnotification";

    public static final String CT_SYSTEM_MESSAGE = "application/x-msmsgssystemmessage";

    public static final String CT_INVITATION = "text/x-msmsgsinvite";

    public static final String CT_P2P = "application/x-msnmsgrp2p";

    public static final String CT_DATACAST = "text/x-msnmsgr-datacast";

    public static final String CT_EMOTICON = "text/x-mms-emoticon";

    public static final String CHARSET = "; charset="
            + JmlConstants.DEFAULT_ENCODING;
}

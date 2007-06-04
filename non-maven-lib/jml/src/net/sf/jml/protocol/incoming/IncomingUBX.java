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
package net.sf.jml.protocol.incoming;

import java.io.UnsupportedEncodingException;

import net.sf.jml.MsnProtocol;
import net.sf.jml.Email;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;

/**
 * When someone changed his person message, the contact on AL will received
 * this notify. OutgoingUUX's notify message.
 * <p>
 * Supported Protocol: MSNP11
 * <p>
 * Syntax: UBX email msgLen\r\n content
 * 
 * @author Roger Chen
 */
public class IncomingUBX extends MsnIncomingMessage {

    public IncomingUBX(MsnProtocol protocol) {
        super(protocol);
    }

    protected boolean isSupportTransactionId() {
        return false;
    }

    protected boolean isSupportChunkData() {
        return true;
    }

    public Email getEmail() {
        return Email.parseStr(getParam(0));
    }

    public String getPersonalMessage() {
        try {
            String data = new String(getChunkData(), "UTF-8");
            int start = data.indexOf("<PSM>")+"<PSM>".length();
            int end = data.indexOf("</PSM>");
            return data.substring(start, end);
        } catch (UnsupportedEncodingException e) {
            // nothing
        } catch (StringIndexOutOfBoundsException e) {
            // nothing
        }
        return "";
    }

    public String getCurrentMedia() {
        try {
            String data = new String(getChunkData(), "UTF-8");
            int start = data.indexOf("<CurrentMedia>")+"<CurrentMedia>".length();
            int end = data.indexOf("</CurrentMedia>");
            return data.substring(start, end);
        } catch (UnsupportedEncodingException e) {
            // nothing
        } catch (StringIndexOutOfBoundsException e) {
            // nothing
        }
        return "";
    }

    protected void messageReceived(MsnSession session) {
       super.messageReceived(session);
       MsnContactImpl contact = (MsnContactImpl) session.getMessenger()
               .getContactList().getContactByEmail(getEmail());

       if (contact != null) {
           contact.setPersonalMessage(getPersonalMessage());
           contact.setCurrentMedia(getCurrentMedia());
           ((AbstractMessenger) session.getMessenger())
                   .fireContactStatusChanged(contact);
       }
    }

}
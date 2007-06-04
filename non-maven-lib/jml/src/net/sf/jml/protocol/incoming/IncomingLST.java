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

import net.sf.jml.Email;
import net.sf.jml.MsnProtocol;
import net.sf.jml.Telephone;
import net.sf.jml.impl.AbstractMessenger;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.impl.MsnContactListImpl;
import net.sf.jml.protocol.MsnIncomingMessage;
import net.sf.jml.protocol.MsnSession;
import net.sf.jml.util.NumberUtils;
import net.sf.jml.util.StringUtils;

/**
 * One of responses of OutgoingSYN, list contact list.
 * <p>
 * Supported Protocol: All
 * <p>
 * MSNP8/MSNP9 Syntax: LST email friendlyName listNum (comma-separated
 * groupId)
 * <p>
 * MSNP10 Syntax 1: LST N=email F=friendlyName C=id listNum (comma-separated
 * groupId)
 * <p>
 * MSNP10 Syntax 2: LST N=email listNum
 * <p>
 * MSNP10 Syntax2 is used when the user is not in FL.
 * 
 * @author Roger Chen
 */
public class IncomingLST extends MsnIncomingMessage {

    public IncomingLST(MsnProtocol protocol) {
        super(protocol);
    }

    protected boolean isSupportTransactionId() {
        return false;
    }

    public Email getEmail() {
        String email = getParam(0);
        if (protocol.before(MsnProtocol.MSNP10)) {
            return Email.parseStr(email);
        }
        return Email.parseStr(email.substring(2)); //N=email
    }
    
    //TODO:竟然还有这样的格式： LST N=tel:+8613812345678 2 4，所以不一定所有的 MsnContact.getEmail != null,代码中涉及 getEmail() 调用的地方应该要修改
    public Telephone getTelephone() {
        String tel = getParam(0);
        return Telephone.parseStr(tel.substring(2)); //N=tel:+8613412345678
        
    }

    public String getFriendlyName() {
        String friendlyName = getParam(1);
        if (!protocol.before(MsnProtocol.MSNP10)) {
            String param = getParam(1);
            if (param != null && param.startsWith("F="))
                friendlyName = param.substring(2); //F=friendName
            else{
                
            	if (getEmail() != null){  //User not in FL, only have email and listNum
                	return getEmail().getEmailAddress();
                }else if (getTelephone() != null){  //telephone, only have telephone number
                	return getTelephone().getTelephoneNumber();
                }
                return getId();
            }
        }
        return StringUtils.urlDecode(friendlyName);
    }

    public String getId() {
        if (protocol.after(MsnProtocol.MSNP9)) {
            String id = getParam(2);
            if (id != null && id.startsWith("C=")) {
                return id.substring(2); //C=Id
            }
        }
        return null;
    }

    public int getListNum() {
        if (protocol.before(MsnProtocol.MSNP10))
            return NumberUtils.stringToInt(getParam(2));
        String param = getParam(1);
        if (param != null && param.startsWith("F="))
            return NumberUtils.stringToInt(getParam(3));
        //User not in FL
        return NumberUtils.stringToInt(getParam(1));
    }

    public String[] getGroupId() {
        int pcount = getParamCount();
        if (pcount < 1) return new String[0];
        String groupIds = getParam(getParamCount()-1);
        if (groupIds != null) {
            return groupIds.split(",");
        }
        return new String[0];
    }

    protected void messageReceived(MsnSession session) {
        super.messageReceived(session);

        MsnContactListImpl contactList = (MsnContactListImpl) session
                .getMessenger().getContactList();
        String[] groupId = getGroupId();
        String id = getId();

        MsnContactImpl contact = new MsnContactImpl(contactList);
        contact.setEmail(getEmail());
        contact.setTelephone(getTelephone());
        
        if (id != null){
        	contact.setId(id);
        }else if (contact.getEmail() != null){
        	contact.setId(contact.getEmail().getEmailAddress());
        }else if (contact.getTelephone() != null){
        	contact.setId(contact.getTelephone().getTelephoneNumber());
        }
        contact.setFriendlyName(getFriendlyName());
        contact.setDisplayName(contact.getFriendlyName());
        contact.setListNumber(getListNum());
        for (int i = 0; i < groupId.length; i++) {
            contact.addBelongGroup(groupId[i]);
        }
        contactList.addContact(contact);

        //Judge sync complete
        if (contactList.getCurrentContactCount() == contactList
                .getContactCount()
                && contactList.getCurrentGroupCount() == contactList
                        .getGroupCount()) {
            ((AbstractMessenger) session.getMessenger())
                    .fireContactListSyncCompleted(); //Sync completed
        }
    }

}

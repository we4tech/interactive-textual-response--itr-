package net.sf.jml.example;

import org.apache.commons.logging.*;

import net.sf.jml.*;
import net.sf.jml.event.*;
import net.sf.jml.message.*;
import net.sf.jml.util.JmlConstants;

public class PrettyMessenger extends BasicMessenger {
	
	private static final Log log = LogFactory.getLog(PrettyMessenger.class);

	protected void initMessenger(MsnMessenger messenger) {
		messenger.getOwner().setInitStatus(MsnUserStatus.ONLINE);
		try {
			MsnObject displayPicture = MsnObject.getInstance(getEmail(),
					"./resource/UserTile/headset.png");
			messenger.getOwner().setInitDisplayPicture(displayPicture);
		} catch (Exception ex) {
			log.warn("can't load user tile.",ex);
		}
		messenger.addListener(new PrettyMsnListener());
	}
	
	private static class PrettyMsnListener extends MsnAdapter {

        public void exceptionCaught(MsnMessenger messenger, Throwable throwable) {
            log.error(messenger + throwable.toString(), throwable);
        }

        public void loginCompleted(MsnMessenger messenger) {
            log.info(messenger + " login complete ");
        }

        public void logout(MsnMessenger messenger) {
            log.info(messenger + " logout");
        }

        public void instantMessageReceived(MsnSwitchboard switchboard,
                                           MsnInstantMessage message,
                                           MsnContact friend) {
        	
        	// set personal message
        	switchboard.getMessenger().getOwner().setPersonalMessage(message.getContent());
        	
        	// send custom emoticons message
        	try {
                MsnObject hey = MsnObject.getInstance(switchboard.getMessenger().
                        getOwner().
                        getEmail().getEmailAddress(),
                        "./resource/CustomEmoticons/hey.gif");
                hey.setType(MsnObject.TYPE_CUSTOM_EMOTICON);
                MsnObject kiss = MsnObject.getInstance(switchboard.getMessenger().
                        getOwner().
                        getEmail().getEmailAddress(),
                        "./resource/CustomEmoticons/kiss.gif");
                kiss.setType(MsnObject.TYPE_CUSTOM_EMOTICON);
                MsnEmoticonMessage emoticon = new MsnEmoticonMessage();
                emoticon.putEmoticon(":hey:", hey);
                emoticon.putEmoticon(":kiss:", kiss);
                switchboard.sendMessage(emoticon, true);
                

            } catch (Exception ex) {
            	log.warn("can'r create Emoticon Message",ex);
            }
        	
        	
            MsnInstantMessage reply = new MsnInstantMessage();
            reply.setBold(false);
            reply.setItalic(false);
            reply
                    .setFontRGBColor((int) (Math.random() * 255 * 255 * 255));
            reply.setContent(":kiss:"+JmlConstants.LINE_SEPARATOR+"hello, " + friend.getFriendlyName()+".:hey: look at my personal message.");
            switchboard.sendMessage(reply);
      
        }

        public void systemMessageReceived(MsnMessenger messenger,
                                          MsnSystemMessage message) {
            log.info(messenger + " recv system message " + message);
        }

        public void controlMessageReceived(MsnSwitchboard switchboard,
                                           MsnControlMessage message,
                                           MsnContact contact) {
            log.info(switchboard + " recv control message from "
                     + contact.getEmail());
            message.setTypingUser(switchboard.getMessenger().getOwner().getEmail().getEmailAddress());
            switchboard.sendMessage(message, false);
        }

        public void datacastMessageReceived(MsnSwitchboard switchboard,
                                            MsnDatacastMessage message,
                                            MsnContact friend) {
            log.info(switchboard + " recv datacast message " + message);
            switchboard.sendMessage(message, false);
        }

        public void unknownMessageReceived(MsnSwitchboard switchboard,
                                           MsnUnknownMessage message,
                                           MsnContact friend) {
            log.info(switchboard + " recv unknown message " + message);
        }

        public void contactListInitCompleted(MsnMessenger messenger) {
            log.info(messenger + " contact list init completeted");
        }

        public void contactListSyncCompleted(MsnMessenger messenger) {
            log.info(messenger + " contact list sync completed");
        }

        public void contactStatusChanged(MsnMessenger messenger,
                                         MsnContact friend) {
            log.info(messenger + " friend " + friend.getEmail()
                     + " status changed from " + friend.getOldStatus() + " to "
                     + friend.getStatus());
        }

        public void ownerStatusChanged(MsnMessenger messenger) {
            log.info(messenger + " status changed from "
                     + messenger.getOwner().getOldStatus() + " to "
                     + messenger.getOwner().getStatus());
        }

        public void contactAddedMe(MsnMessenger messenger, MsnContact friend) {
            log.info(friend.getEmail() + " add " + messenger);
        }

        public void contactRemovedMe(MsnMessenger messenger, MsnContact friend) {
            log.info(friend.getEmail() + " remove " + messenger);
        }

        public void switchboardClosed(MsnSwitchboard switchboard) {
            log.info(switchboard + " closed");
        }

        public void switchboardStarted(MsnSwitchboard switchboard) {
            log.info(switchboard + " started");
        }

        public void contactJoinSwitchboard(MsnSwitchboard switchboard,
                                           MsnContact friend) {
            log.info(friend.getEmail() + " join " + switchboard);
        }

        public void contactLeaveSwitchboard(MsnSwitchboard switchboard,
                                            MsnContact friend) {
            log.info(friend.getEmail() + " leave " + switchboard);
        }

    }

}

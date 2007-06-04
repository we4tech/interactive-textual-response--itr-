package net.sf.jml.example;

import net.sf.jml.MsnContact;
import net.sf.jml.MsnMessenger;
import net.sf.jml.MsnProtocol;
import net.sf.jml.MsnSwitchboard;
import net.sf.jml.MsnUserStatus;
import net.sf.jml.event.MsnAdapter;
import net.sf.jml.event.MsnContactListAdapter;
import net.sf.jml.impl.MsnContactImpl;
import net.sf.jml.impl.MsnMessengerFactory;
import net.sf.jml.message.MsnControlMessage;
import net.sf.jml.message.MsnDatacastMessage;
import net.sf.jml.message.MsnInstantMessage;
import net.sf.jml.message.MsnSystemMessage;
import net.sf.jml.message.MsnUnknownMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Roger Chen
 */
public class SimpleMsn
{

	private static final Log log = LogFactory.getLog(SimpleMsn.class);

	private String email;

	private String password;

	private MsnMessenger messenger;

	public SimpleMsn(String email, String password)
	{
		this.email = email;
		this.password = password;
	}

	public void start()
	{
		messenger = MsnMessengerFactory.createMsnMessenger(email,
				password);
		messenger
				.setSupportedProtocol(new MsnProtocol[] { MsnProtocol.MSNP11 });
		messenger.getOwner().setInitStatus(MsnUserStatus.ONLINE);
		messenger.setLogIncoming(true);
		messenger.setLogOutgoing(true);
		messenger.addListener(new MsnListener());
		messenger.login();

		messenger.addContactListListener(new MsnContactListAdapter()
		{
			public void contactStatusChanged(MsnMessenger msn, MsnContact con)
			{
				System.out.println(con.getDisplayName());
				System.out.println(((MsnContactImpl) con).getPersonalMessage());
				System.out.println(((MsnContactImpl) con).getCurrentMedia());
			}

			public void contactListInitCompleted(MsnMessenger messenger)
			{
				listContacts();
			}


		});
		
		try
		{
			Thread.sleep(10000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		messenger.logout();
		messenger.login();
		
		
//		messenger.getOwner().setDisplayName("Blah");
//		messenger.getOwner().getDisplayName();
	}
	
	private void listContacts()
	{
		MsnContact[] cons = messenger.getContactList().getContacts();
		for (int i = 0; i < cons.length; i++)
		{
			System.out.println(cons[i].getDisplayName());
			System.out.println(cons[i].getEmail());
			System.out.println(cons[i].getStatus());
			System.out.println(((MsnContactImpl)cons[i]).getPersonalMessage());
		}
	}

	public static void main(String[] args) throws Exception
	{
		new SimpleMsn("email", "password").start();
	}

	private static class MsnListener extends MsnAdapter
	{

		public void exceptionCaught(MsnMessenger messenger, Throwable throwable)
		{
			log.error(messenger + throwable.toString(), throwable);
		}

		public void loginCompleted(MsnMessenger messenger)
		{
			log.info(messenger + " login complete ");
		}

		public void logout(MsnMessenger messenger)
		{
			log.info(messenger + " logout");
		}

		public void instantMessageReceived(MsnSwitchboard switchboard,
				MsnInstantMessage message, MsnContact friend)
		{
			log.info(switchboard + " recv instant message " + message);
			switchboard.sendMessage(message, false);
		}

		public void systemMessageReceived(MsnMessenger messenger,
				MsnSystemMessage message)
		{
			log.info(messenger + " recv system message " + message);
		}

		public void controlMessageReceived(MsnSwitchboard switchboard,
				MsnControlMessage message, MsnContact contact)
		{
			log.info(switchboard + " recv control message from "
					+ contact.getEmail());
			switchboard.sendMessage(message, false);
		}

		public void datacastMessageReceived(MsnSwitchboard switchboard,
				MsnDatacastMessage message, MsnContact friend)
		{
			log.info(switchboard + " recv datacast message " + message);

			switchboard.sendMessage(message, false);
		}

		public void unknownMessageReceived(MsnSwitchboard switchboard,
				MsnUnknownMessage message, MsnContact friend)
		{
			log.info(switchboard + " recv unknown message " + message);
		}

		public void contactListInitCompleted(MsnMessenger messenger)
		{
			log.info(messenger + " contact list init completeted");
		}

		public void contactListSyncCompleted(MsnMessenger messenger)
		{
			log.info(messenger + " contact list sync completed");
		}

		public void contactStatusChanged(MsnMessenger messenger,
				MsnContact friend)
		{
			log.info(messenger + " friend " + friend.getEmail()
					+ " status changed from " + friend.getOldStatus() + " to "
					+ friend.getStatus());
		}

		public void ownerStatusChanged(MsnMessenger messenger)
		{
			log.info(messenger + " status changed from "
					+ messenger.getOwner().getOldStatus() + " to "
					+ messenger.getOwner().getStatus());
		}

		public void contactAddedMe(MsnMessenger messenger, MsnContact friend)
		{
			log.info(friend.getEmail() + " add " + messenger);
		}

		public void contactRemovedMe(MsnMessenger messenger, MsnContact friend)
		{
			log.info(friend.getEmail() + " remove " + messenger);
		}

		public void switchboardClosed(MsnSwitchboard switchboard)
		{
			log.info(switchboard + " closed");
		}

		public void switchboardStarted(MsnSwitchboard switchboard)
		{
			log.info(switchboard + " started");
		}

		public void contactJoinSwitchboard(MsnSwitchboard switchboard,
				MsnContact friend)
		{
			log.info(friend.getEmail() + " join " + switchboard);
		}

		public void contactLeaveSwitchboard(MsnSwitchboard switchboard,
				MsnContact friend)
		{
			log.info(friend.getEmail() + " leave " + switchboard);
		}

	}

}
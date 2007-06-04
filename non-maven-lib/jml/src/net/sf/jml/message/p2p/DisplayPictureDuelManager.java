package net.sf.jml.message.p2p;

import java.util.*;

import net.sf.jml.MsnObject;
import net.sf.jml.protocol.MsnSession;

public class DisplayPictureDuelManager {

	protected static DisplayPictureDuelManager dm = new DisplayPictureDuelManager();

	public static synchronized DisplayPictureDuelManager getDuelManager() {
		return dm;
	}

	private List duels;

	private Hashtable pictures;
	private MsnObject displayPicutre;
	
	private RemoveMsnObjectThread removeMsnObjectThread;

	private DisplayPictureDuelManager() {
		duels = new ArrayList();
		pictures = new Hashtable();
		removeMsnObjectThread = new RemoveMsnObjectThread(pictures);
		removeMsnObjectThread.setDaemon(true);
		removeMsnObjectThread.start();
	}

	public synchronized void add(DisplayPictureDuel duel) {
		duels.add(duel);
	}

	public synchronized DisplayPictureDuel get(int baseId) {
		for (Iterator iter = duels.iterator(); iter.hasNext();) {
			DisplayPictureDuel item = (DisplayPictureDuel) iter.next();
			if (item.getBaseId() == baseId) {
				return item;
			}
		}
		return null;
	}

	public synchronized boolean remove(int baseId) {
		DisplayPictureDuel duel = get(baseId);
		return duels.remove(duel);
	}

	public int getSize() {
		return duels.size();
	}

	public void putPicture(String key, MsnObject obj) {
		RemovableMsnObject msnObj = new RemovableMsnObject(obj);
		pictures.put(key, msnObj);
	}

	public MsnObject getPicture(String key) {
		RemovableMsnObject ret = (RemovableMsnObject) pictures.get(key);
		if (ret != null) {
			return ret.getMsnObject();
		}
		return getDisplayPicutre();
	}

	public MsnObject getDisplayPicutre() {
		return displayPicutre;
	}

	public void setDisplayPicutre(MsnObject displayPicutre) {
		this.displayPicutre = displayPicutre;
	}

	class RemovableMsnObject {

		private MsnObject msnObject;

		private long lastAccessedTime;

		public RemovableMsnObject(MsnObject msnObject) {
			this.msnObject = msnObject;
			this.lastAccessedTime = Calendar.getInstance().getTimeInMillis();
		}

		public long getLastAccessedTime() {
			return lastAccessedTime;
		}

		public MsnObject getMsnObject() {
			this.lastAccessedTime = Calendar.getInstance().getTimeInMillis();
			return msnObject;
		}
	}
	
	class RemoveMsnObjectThread extends Thread {
		
		private Hashtable pictures;
		
		public RemoveMsnObjectThread(Hashtable pictures) {
			this.pictures = pictures;
		}
		
        public void run() {
            while (true) {
                try {
                    sleep(1000*60*10);
                    long now = Calendar.getInstance().getTimeInMillis();
                    List keys = new ArrayList(pictures.keySet());
                    for (Iterator iter = keys.iterator(); iter.hasNext();) {
            			String item = (String) iter.next();
            			RemovableMsnObject pic = (RemovableMsnObject)pictures.get(item);
            			if (pic != null && (now - pic.getLastAccessedTime() > 1000*60*20)){
            				pictures.remove(item);
              			}
            		}
                } catch (InterruptedException ex) {
                    return;
                } catch (Exception e) {
                }
            }
        }
    }
}

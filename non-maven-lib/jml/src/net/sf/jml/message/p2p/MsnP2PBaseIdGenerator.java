package net.sf.jml.message.p2p;

import net.sf.jml.util.NumberUtils;

public class MsnP2PBaseIdGenerator {

	private static MsnP2PBaseIdGenerator idgen = new MsnP2PBaseIdGenerator();

	private int id = NumberUtils.getIntRandom();

	private MsnP2PBaseIdGenerator() {
	}

	public static MsnP2PBaseIdGenerator getInstance() {
		return idgen;
	}

	public synchronized int getNextId() {
		if (id >= Integer.MAX_VALUE) {
			id = 0;
		} else {
			id++;
		}
		return id;
	}
	
	public int getId() {
		return id;
	}
	
	

}

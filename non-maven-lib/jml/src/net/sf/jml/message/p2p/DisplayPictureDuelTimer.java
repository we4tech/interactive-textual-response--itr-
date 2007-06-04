package net.sf.jml.message.p2p;

import java.util.*;

public class DisplayPictureDuelTimer {

    protected static DisplayPictureDuelTimer duelTimer;
    public static synchronized DisplayPictureDuelTimer getDuelTimer() {
        if (duelTimer == null)
            duelTimer = new DisplayPictureDuelTimer();
        return duelTimer;
    }

    private java.util.Timer timer;
    private DisplayPictureDuelTimer() {
        timer = new Timer();
    }

    public void schedule(TimerTask task, long delay) {
        timer.schedule(task, delay);
    }
}

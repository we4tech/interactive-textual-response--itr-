package net.sf.jml.message.p2p;

public class DisplayPictureDuelTimerTask extends java.util.TimerTask {

    private int baseId;
    private int lastStatus;

    public DisplayPictureDuelTimerTask(int baseId, int currentStatus) {
        this.baseId = baseId;
        this.lastStatus = currentStatus;
    }

    public void run() {
        DisplayPictureDuelManager dm = DisplayPictureDuelManager.getDuelManager();
        try {
            DisplayPictureDuel d = dm.get(baseId);
            if (d != null && lastStatus == d.getDuelTimerStatus()) {
                dm.remove(baseId);
            }
        } catch (Exception e) {
        }
    }
}
package com.pps.news.common;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

/**
 * Hold a wakelock that can be acquired in the AlarmReceiver and
 * released in the AlarmAlert activity
 */
public class AlarmAlertWakeLock {

	private final static String LOGTAG = "AlarmClock";
    private static PowerManager.WakeLock sCpuWakeLock;

    public static void acquireCpuWakeLock(Context context) {
    	Log.v("AlarmAlertWakeLock", "Acquiring cpu wake lock");
        if (sCpuWakeLock != null) {
            return;
        }

        PowerManager pm =
                (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        sCpuWakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, LOGTAG);
        sCpuWakeLock.acquire();
    }

    public static void releaseCpuLock() {
        Log.v("AlarmAlertWakeLock", "Releasing cpu wake lock");
        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }
    }
}

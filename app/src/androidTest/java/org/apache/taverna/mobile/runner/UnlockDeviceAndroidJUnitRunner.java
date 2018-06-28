package org.apache.taverna.mobile.runner;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.support.test.runner.AndroidJUnitRunner;

public class UnlockDeviceAndroidJUnitRunner extends AndroidJUnitRunner {

    private PowerManager.WakeLock mWakeLock;

    @SuppressLint("MissingPermission")
    @Override
    public void onStart() {
        Application application = (Application) getTargetContext().getApplicationContext();
        String simpleName = UnlockDeviceAndroidJUnitRunner.class.getSimpleName();
        // Unlock the device so that the tests can input keystrokes.
        ((KeyguardManager) application.getSystemService(Context.KEYGUARD_SERVICE))
                .newKeyguardLock(simpleName)
                .disableKeyguard();
        // Wake up the screen.
        PowerManager powerManager = ((PowerManager) application.getSystemService(Context
                .POWER_SERVICE));
        mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, simpleName);
        mWakeLock.acquire();
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
    }
}
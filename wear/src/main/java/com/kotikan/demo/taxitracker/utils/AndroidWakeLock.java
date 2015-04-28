package com.kotikan.demo.taxitracker.utils;

import android.content.Context;
import android.os.PowerManager;

public class AndroidWakeLock implements WakeLock {

    private PowerManager.WakeLock lock;

    @Override
    public void onPause() {
        System.out.println("AndroidWakeLock.onPause");
        if (lock.isHeld()) {
            System.out.println(" calling release");
            lock.release();
        } else {
            System.out.println(" not doing anything");
        }
    }

    @Override
    public void onStart() {
        System.out.println("AndroidWakeLock.onStart");
        if (!lock.isHeld()) {
            lock.acquire();
            System.out.println(" calling acquire");
        } else {
            System.out.println(" lock not held, no action");
        }
    }

    @Override
    public void onCreate(Context context) {
        System.out.println("AndroidWakeLock.onCreate");

        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        lock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "MyWakelockTag");
    }
}

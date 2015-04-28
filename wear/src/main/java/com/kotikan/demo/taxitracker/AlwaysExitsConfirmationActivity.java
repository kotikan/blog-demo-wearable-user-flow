package com.kotikan.demo.taxitracker;

import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.ConfirmationActivity;

public class AlwaysExitsConfirmationActivity extends ConfirmationActivity {

    private final WakeLock wakeLock = new AndroidWakeLock();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Handler handler = new Handler();
        handler.postDelayed(killActivity(), 2000);
        wakeLock.onCreate(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        wakeLock.onStart();
    }

    private Runnable killActivity() {
        return new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    finish();
                }
            }
        };
    }
}

package com.kotikan.demo.taxitracker.activities;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.ConfirmationActivity;

import com.kotikan.demo.taxitracker.utils.WakeLock;
import com.kotikan.demo.taxitracker.utils.AndroidWakeLock;

public class AlwaysExitsConfirmationActivity extends ConfirmationActivity {

    private final WakeLock wakeLock = new AndroidWakeLock();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Handler handler = new Handler();
        handler.postDelayed(killActivity(), 2000);

        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        if (extras != null) {
            if (ConfirmationActivity.SUCCESS_ANIMATION == extras.getInt(ConfirmationActivity.EXTRA_ANIMATION_TYPE)) {
                ((NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE)).cancelAll();
            }
        }

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

package com.kotikan.demo.taxitracker;

import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.ConfirmationActivity;

public class AlwaysExitsConfirmationActivity extends ConfirmationActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Handler handler = new Handler();
        handler.postDelayed(killActivity(), 2000);

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

package com.kotikan.demo.taxitracker;

import android.content.Context;

public interface WakeLock {
    void onPause();

    void onStart();

    void onCreate(Context context);
}

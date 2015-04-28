package com.kotikan.demo.taxitracker.utils;

import android.content.Context;

public interface WakeLock {
    void onPause();

    void onStart();

    void onCreate(Context context);
}

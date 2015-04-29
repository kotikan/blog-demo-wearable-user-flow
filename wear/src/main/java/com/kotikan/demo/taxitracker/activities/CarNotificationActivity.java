package com.kotikan.demo.taxitracker.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CarNotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TextView view = new TextView(this);
        view.setText("HI THERE");
        setContentView(view);
    }
}

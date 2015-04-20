package com.kotikan.demo.taxitracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.feature_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSuccess();
            }
        });

        findViewById(R.id.feature_decline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickCancel();
            }
        });
    }

    private void onClickCancel() {

    }

    private void onClickSuccess() {
    }
}

package com.kotikan.demo.taxitracker;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.ConfirmationActivity;
import android.view.View;
import android.widget.TextView;

public class YesNoActivity extends Activity {

    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    private final WakeLock wakeLock = new AndroidWakeLock();
    private String userMessage = "Play sax at 6pm";

    public static void startWithData(Service service, String message) {
        final Intent intent = new Intent(service, YesNoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_MESSAGE, message);

        service.startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        wakeLock.onStart();
    }

    @Override
    protected void onPause() {
        super.onStop();
        wakeLock.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sax);

        wakeLock.onCreate(this);

        final Intent intent = getIntent();
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null) {
                final String extraMessage = extras.getString(EXTRA_MESSAGE);
                if (extraMessage != null) {
                    userMessage = extraMessage;
                }
            }
        }

        final Vibrator systemService = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        systemService.vibrate(75l);

        ((TextView) findViewById(R.id.message)).setText(userMessage);
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
        launchNotifier("Event cancelled", ConfirmationActivity.FAILURE_ANIMATION);
        exit();
    }

    private void onClickSuccess() {
        launchNotifier("Your booked!", ConfirmationActivity.SUCCESS_ANIMATION);
        exit();
    }

    private void launchNotifier(String message, int successAnimation) {
        final Intent intent = new Intent(this, AlwaysExitsConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, successAnimation);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private void exit() {
        finish();
    }
}

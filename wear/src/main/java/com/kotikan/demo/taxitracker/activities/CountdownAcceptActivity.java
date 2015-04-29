package com.kotikan.demo.taxitracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;
import android.widget.TextView;

import com.kotikan.demo.taxitracker.R;
import com.kotikan.demo.taxitracker.utils.Extras;
import com.kotikan.demo.taxitracker.utils.WakeLock;
import com.kotikan.demo.taxitracker.utils.AndroidWakeLock;

public class CountdownAcceptActivity extends Activity {

    final static private int car_countdown_in_seconds = 5;
    final static private int car_countdown_in_millis = car_countdown_in_seconds * 1000;

    private String userMessage = "Car arriving in %ss";
    private final WakeLock wakeLock = new AndroidWakeLock();

    public static void startWithData(Activity service, String message) {
        final Intent intent = new Intent(service, CountdownAcceptActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Extras.EXTRA_MESSAGE, message);

        service.startActivity(intent);
    }

    private DelayedConfirmationView confirmationView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wakeLock.onCreate(this);

        final Intent intent = getIntent();
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null) {
                final String extraMessage = extras.getString(Extras.EXTRA_MESSAGE);
                if (extraMessage != null) {
                    userMessage = extraMessage;
                }
            }
        }

        final Vibrator systemService = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        systemService.vibrate(75l);

        confirmationView = (DelayedConfirmationView) findViewById(R.id.book_timer);
        confirmationView.setListener(new DelayedConfirmationView.DelayedConfirmationListener() {
            @Override
            public void onTimerFinished(View view) {
                onClickSuccess();
            }

            @Override
            public void onTimerSelected(View view) {
                onClickCancel();
            }
        });
        confirmationView.setTotalTimeMs(car_countdown_in_millis);
        confirmationView.start();

        final TextView message = (TextView) findViewById(R.id.message);
        message.setText(userMessage);
    }

    private void onClickCancel() {
        launchNotifier("Car cancelled", ConfirmationActivity.FAILURE_ANIMATION);
        exit();
    }

    private void onClickSuccess() {
        launchNotifier("Car booked", ConfirmationActivity.SUCCESS_ANIMATION);
        exit();
    }

    private void launchNotifier(String message, int successAnimation) {
        final Intent intent = new Intent(this, AlwaysExitsConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, successAnimation);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    private void exit() {
        confirmationView.setListener(null);
        finish();
    }
}

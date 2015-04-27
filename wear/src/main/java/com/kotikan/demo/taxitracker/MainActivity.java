package com.kotikan.demo.taxitracker;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private static final String EXTRA_TAXI_ARRIVE_IN = "EXTRA_TAXI_ARRIVE_IN";
    final static private int car_will_arrive_in = 25;
    final static private int car_countdown_in_seconds = 7;
    final static private int car_countdown_in_millis = car_countdown_in_seconds * 1000;

    private String userMessage = "Car arriving in %ss";
    private int countdownToCarArrive = car_will_arrive_in;

    public static void startWithData(Service service, String message, String arrivingIn) {
        final Intent intent = new Intent(service, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TAXI_ARRIVE_IN, arrivingIn);

        service.startActivity(intent);
    }

    private DelayedConfirmationView confirmationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = getIntent();
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null) {
                final String extraMessage = extras.getString(EXTRA_MESSAGE);
                if (extraMessage != null) {
                    userMessage = extraMessage;
                }
                final String string = extras.getString(EXTRA_TAXI_ARRIVE_IN);
                if (string != null) {
                    countdownToCarArrive = Integer.valueOf(string);
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
        final Handler handler = new Handler();
        updateTo(countdownToCarArrive, message, handler);
    }

    private void updateTo(final int secondsLeft, final TextView message, final Handler handler) {
        message.setText(String.format(userMessage, secondsLeft));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateTo(secondsLeft - 1, message, handler);
            }
        }, 1000);
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

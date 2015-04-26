package com.kotikan.demo.taxitracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private DelayedConfirmationView confirmationView;
    final private int car_will_arrive_in = 25;
    final private int car_countdown_in_seconds = 7;
    final private int car_countdown_in_millis = car_countdown_in_seconds * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        updateTo(car_will_arrive_in, message, handler);
    }

    private void updateTo(final int secondsLeft, final TextView message, final Handler handler) {
        message.setText("Car arriving in " + secondsLeft + "s");

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
        launchNotifier("Car en route", ConfirmationActivity.SUCCESS_ANIMATION);
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

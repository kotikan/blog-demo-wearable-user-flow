package com.kotikan.demo.taxitracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.DelayedConfirmationView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    private DelayedConfirmationView confirmationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        confirmationView.setTotalTimeMs(8000l);
        confirmationView.start();
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

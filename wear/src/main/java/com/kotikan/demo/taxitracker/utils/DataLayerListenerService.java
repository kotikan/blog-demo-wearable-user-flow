package com.kotikan.demo.taxitracker.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Binder;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.kotikan.demo.taxitracker.R;
import com.kotikan.demo.taxitracker.activities.CarNotificationActivity;
import com.kotikan.demo.taxitracker.activities.YesNoActivity;

public class DataLayerListenerService extends WearableListenerService {

    private static final String NOTIFICATION_GROUP_KEY_CAR = "NOTIFICATION_GROUP_KEY_CAR";
    private static final int NOTIFICATION_KEY_ID_CAR = 1;


    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        if (messageEvent != null) {
            final String path = messageEvent.getPath();
            final String carActivity = "/start/carActivity/";
            final String saxophonistActivity = "/start/saxActivity/";
            long token = Binder.clearCallingIdentity();
            try {
                if (path.startsWith(carActivity)) {
                    final String userMessage = path.replace(carActivity, "");

                    final RandomGenerator generator = new RandomGenerator();

                    final BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.cars_01);

                    NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                    final Notification.Builder builder = buildCarMainPage(userMessage);
                    builder.extend(new Notification.WearableExtender()
                            .setBackground(drawable.getBitmap())
                            .addPage(buildCarPage(userMessage, generator, R.drawable.cars_01).build())
                            .addPage(buildCarPage(userMessage, generator, R.drawable.cars_02).build())
                            .addPage(buildCarPage(userMessage, generator, R.drawable.cars_03).build()));

                    manager.notify(NOTIFICATION_KEY_ID_CAR, builder.build());

                } else if (path.startsWith(saxophonistActivity)) {
                    final String replace = path.replace(saxophonistActivity, "");
                    YesNoActivity.startWithData(this, replace);
                }
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
    }

    private Notification.Builder buildCarMainPage(String title) {
        final Notification.Builder builder = new Notification.Builder(this);

        builder.setContentTitle(title);
        builder.setSmallIcon(R.drawable.cars_01);

        return builder;
    }

    private Notification.Builder buildCarPage(String userMessage, RandomGenerator generator, int carResId) {
        final Notification.Builder builder = new Notification.Builder(this);

        final Intent intent = new Intent(this, CarNotificationActivity.class);
        intent.putExtra(Extras.EXTRA_CAR_PRICE, generator.newPrice());
        intent.putExtra(Extras.EXTRA_CAR_ARRIVES_IN, generator.newTime());
        intent.putExtra(Extras.EXTRA_CAR_DRAWABLE_ID, carResId);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.extend(new Notification.WearableExtender()
                .setDisplayIntent(pendingIntent)
                .setCustomSizePreset(Notification.WearableExtender.SIZE_LARGE));

        builder.setContentTitle(userMessage);
        builder.setSmallIcon(carResId);
//        builder.setVibrate(new long[]{50, 100, 50});

        return builder;
    }
}

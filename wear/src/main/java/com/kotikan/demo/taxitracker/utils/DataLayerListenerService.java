package com.kotikan.demo.taxitracker.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.kotikan.demo.taxitracker.R;
import com.kotikan.demo.taxitracker.activities.CarNotificationActivity;
import com.kotikan.demo.taxitracker.activities.YesNoActivity;

public class DataLayerListenerService extends WearableListenerService {

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
//                    CarStep1Activity.startWithData(this, userMessage);

                    NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                    final Notification.Builder builder = new Notification.Builder(this);

                    final Intent intent = new Intent(this, CarNotificationActivity.class);
                    intent.putExtra(Extras.EXTRA_MESSAGE, userMessage);
                    final PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.extend(new Notification.WearableExtender()
                            .setDisplayIntent(pendingIntent)
                            .setCustomSizePreset(Notification.WearableExtender.SIZE_MEDIUM));

                    builder.setContentTitle(userMessage);
                    builder.setSmallIcon(R.drawable.cars_01);
                    builder.setVibrate(new long[]{5, 20, 5});

                    manager.notify(1, builder.build());

                } else if (path.startsWith(saxophonistActivity)) {
                    final String replace = path.replace(saxophonistActivity, "");
                    YesNoActivity.startWithData(this, replace);
                }
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
    }
}

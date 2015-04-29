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
import com.kotikan.demo.taxitracker.activities.CountdownAcceptActivity;
import com.kotikan.demo.taxitracker.activities.YesNoActivity;

public class DataLayerListenerService extends WearableListenerService {

    private static final int NOTIFICATION_KEY_ID_CAR = 1;
    private static final int NOTIFICATION_KEY_ID_JAZZ = 2;

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

                    final BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.notification_background_car);

                    final NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                    final Notification.Builder carMainPage = new Notification.Builder(this);

                    carMainPage.setContentTitle(userMessage);
                    carMainPage.setSmallIcon(R.drawable.cars_01);

                    carMainPage.extend(new Notification.WearableExtender()
                            .setBackground(drawable.getBitmap())
                            .setHintHideIcon(true)
                            .addPage(carInfoPage())
                            .addAction(carAction(R.drawable.cars_01))
                            .addAction(carAction(R.drawable.cars_02))
                            .addAction(carAction(R.drawable.cars_03))
                    );

                    carMainPage.setPriority(Notification.PRIORITY_MAX);
                    carMainPage.setVibrate(new long[]{50,100,50});

                    manager.notify(NOTIFICATION_KEY_ID_CAR, carMainPage.build());

                } else if (path.startsWith(saxophonistActivity)) {
                    final String location = path.replace(saxophonistActivity, "");
                    YesNoActivity.startWithData(this, location);

                    final BitmapDrawable background = (BitmapDrawable) getResources().getDrawable(R.drawable.notification_background_jazz);

                    final NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                    final Notification.Builder jazzMainPage = new Notification.Builder(this);

                    final RandomGenerator generator = new RandomGenerator();

                    jazzMainPage.setContentTitle("Virtuoso Saxophonist needed");
                    jazzMainPage.setSmallIcon(R.drawable.cars_01);

                    final String price = generator.newPrice(50, 100);
                    jazzMainPage.extend(new Notification.WearableExtender()
                                    .setBackground(background.getBitmap())
                                    .setHintHideIcon(true)
                                    .addPage(jazzInfoPage(price, location))
                                    .addAction(jazzAction(R.drawable.music, price))
                    );

                    jazzMainPage.setPriority(Notification.PRIORITY_MAX);
                    jazzMainPage.setVibrate(new long[]{50, 100, 50});

                    manager.notify(NOTIFICATION_KEY_ID_JAZZ, jazzMainPage.build());


                }
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
    }

    private Notification.Action jazzAction(int icon, String price) {
        final Intent intent = new Intent(this, YesNoActivity.class);
        intent.putExtra(Extras.EXTRA_MESSAGE, "I dig it!");

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, icon, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new Notification.Action(icon, price, pendingIntent);
    }

    private Notification jazzInfoPage(String price, final String location) {
        final Notification.Builder builder = new Notification.Builder(this);
        final Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle(builder);
        bigTextStyle.bigText(
                location + "\n" +
                "2km away\n" +
                "8pm\n" +
                price);
        builder.setStyle(bigTextStyle);
        return builder.build();
    }

    private Notification.Action carAction(int icon) {
        final RandomGenerator generator = new RandomGenerator();

        final Intent intent = new Intent(this, CountdownAcceptActivity.class);
        final String carPrice = generator.newPrice(1, 3);
        final String carArriveIn = generator.newTime();
        intent.putExtra(Extras.EXTRA_MESSAGE, carPrice + "\n" + "See you in " + carArriveIn);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, icon, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new Notification.Action(icon, carPrice, pendingIntent);
    }

    private Notification carInfoPage() {
        final Notification.Builder builder = new Notification.Builder(this);
        final Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle(builder);
        bigTextStyle.bigText("Pick a car, start walking and it will come find you!");
        builder.setStyle(bigTextStyle);
        return builder.build();
    }
}

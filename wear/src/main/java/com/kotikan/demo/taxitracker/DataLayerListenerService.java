package com.kotikan.demo.taxitracker;

import android.os.Binder;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

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
                    final String replace = path.replace(carActivity, "");
                    final String[] splits = replace.split("/");
                    CountdownAcceptActivity.startWithData(this, splits[0], splits[1]);

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

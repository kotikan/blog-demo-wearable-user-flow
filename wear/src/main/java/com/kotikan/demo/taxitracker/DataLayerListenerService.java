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
            final String prefix = "/start/MainActivity/";
            if (path.startsWith(prefix)) {
                long token = Binder.clearCallingIdentity();
                try {
                    final String replace = path.replace(prefix, "");
                    final String[] splits = replace.split("/");
                    MainActivity.startWithData(this, splits[0], splits[1]);
                } finally {
                    Binder.restoreCallingIdentity(token);
                }
            }
        }
    }
}

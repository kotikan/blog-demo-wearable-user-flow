package com.kotikan.demo.taxitracker;

import android.os.Binder;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class DataLayerListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        if (messageEvent != null && "/start/MainActivity".equalsIgnoreCase(messageEvent.getPath())) {
            long token = Binder.clearCallingIdentity();
            try {
                MainActivity.startWithData(this, messageEvent.getData());
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
    }
}

package com.kotikan.demo.taxitracker;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.kotikan.demo.taxitracker.view.CarView;

public class CarActivityLauncher {

    private final CarView carView;

    public CarActivityLauncher(CarView carView) {
        this.carView = carView;
    }

    public void launch(final GoogleApiClient googleApiClient, final Node node, int viewId) {
        String message = carView.getMessageText(viewId);
        String carArrivesIn = carView.getCarArrivesIn();

        final String dataBundle = message + "/" + carArrivesIn;

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                Wearable.MessageApi.sendMessage(
                        googleApiClient, node.getId(), "/start/carActivity/" + dataBundle, new byte[0]).setResultCallback(
                        new ResultCallback<MessageApi.SendMessageResult>() {
                            @Override
                            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                if (!sendMessageResult.getStatus().isSuccess()) {
                                    Log.e("error", "Failed to send message with status code: "
                                            + sendMessageResult.getStatus().getStatusCode());
                                }
                            }
                        }
                );
                return null;
            }
        }.execute();
    }
}

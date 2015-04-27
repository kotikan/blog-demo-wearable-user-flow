package com.kotikan.demo.taxitracker;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.kotikan.demo.taxitracker.view.SaxView;

public class SaxActivityLauncher {

    private final SaxView saxView;

    public SaxActivityLauncher(SaxView saxView) {
        this.saxView = saxView;
    }

    public void launch(final GoogleApiClient googleApiClient, final Node node, final int viewId) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                Wearable.MessageApi.sendMessage(
                        googleApiClient, node.getId(), "/start/saxActivity/" + saxView.getMessageText(viewId), new byte[0]).setResultCallback(
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

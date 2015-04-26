package com.kotikan.demo.taxitracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;


public class CarLauncher extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

        findViewById(R.id.car_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRemoteActivity();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void startRemoteActivity() {
        final Set<String> results = new HashSet<String>();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                System.out.println("loading the nodes returned from the google client");
                String nodeId = null;
                for (Node node : nodes.getNodes()) {
                    nodeId = node.getId();
                    System.out.println(String.format("id: %1$s name: %2$s", nodeId, node.getDisplayName()));
                    results.add(nodeId);
                }

                final String dataBundle = "Car arrival in %ss/30";
                Wearable.MessageApi.sendMessage(
                        mGoogleApiClient, nodeId, "/start/MainActivity/" + dataBundle, new byte[0]).setResultCallback(
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

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "google client connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}

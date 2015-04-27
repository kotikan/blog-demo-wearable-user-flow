package com.kotikan.demo.taxitracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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


public class WearLauncherActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;
    private TextView connectedTo;
    private Node connectedNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

        connectedTo = (TextView) findViewById(R.id.connected_to);

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
        connectedTo.setText("Connecting...");
    }

    private void startRemoteActivity() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                final String dataBundle = "Car arrival in %ss/30";
                Wearable.MessageApi.sendMessage(
                        mGoogleApiClient, connectedNode.getId(), "/start/MainActivity/" + dataBundle, new byte[0]).setResultCallback(
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

        new AsyncTask<Void, Void, Node>(){

            @Override
            protected void onPostExecute(Node node) {
                connectedTo.setText("Connected to: " + node.getDisplayName());
            }

            @Override
            protected Node doInBackground(Void... voids) {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                System.out.println("loading the nodes returned from the google client");
                for (Node node : nodes.getNodes()) {
                    connectedNode = node;
                    System.out.println(String.format("id: %1$s name: %2$s", node.getId(), node.getDisplayName()));
                }

                return connectedNode;
            }
        }.execute();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}

package com.kotikan.demo.taxitracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.kotikan.demo.taxitracker.view.AndroidCarView;

public class WearLauncherActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;
    private TextView connectedTo;
    private Node connectedNode;
    private CarActivityLauncher carActivityLauncher = new CarActivityLauncher(new AndroidCarView());
    private View.OnClickListener carClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            carActivityLauncher.launch(mGoogleApiClient, connectedNode, view.getId());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

        connectedTo = (TextView) findViewById(R.id.connected_to);

        findViewById(R.id.radio_airport).setOnClickListener(carClickListener);
        findViewById(R.id.radio_meeting).setOnClickListener(carClickListener);
        findViewById(R.id.radio_haircut).setOnClickListener(carClickListener);

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

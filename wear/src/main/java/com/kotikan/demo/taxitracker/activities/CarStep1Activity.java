package com.kotikan.demo.taxitracker.activities;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.CardView;
import android.support.wearable.view.CardFrame;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kotikan.demo.taxitracker.R;
import com.kotikan.demo.taxitracker.utils.AndroidWakeLock;
import com.kotikan.demo.taxitracker.utils.WakeLock;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class CarStep1Activity extends Activity {

    private static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    private static final String EXTRA_TAXI_ARRIVE_IN = "EXTRA_TAXI_ARRIVE_IN";

    private final WakeLock wakeLock = new AndroidWakeLock();

    public static void startWithData(Service service, String message, String arrivingIn) {
        final Intent intent = new Intent(service, CarStep1Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TAXI_ARRIVE_IN, arrivingIn);

        service.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        wakeLock.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_step_1);
        wakeLock.onCreate(this);
        final Vibrator systemService = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        systemService.vibrate(75l);


        final WearableListView view = (WearableListView) findViewById(R.id.wearable_list);
        final List<CarData> data = new ArrayList<CarData>() {
            {
                add(new CarData("Ross's car", "£200 pounds per minute"));
                add(new CarData("Jonathan's car", "Dangrous to take that chance"));
                add(new CarData("Rob's car", "Average"));
                add(new CarData("Charlie's car", "it's a bike"));
            }
        };
        view.setAdapter(new CarAdapter(data, this));
    }

    private static class CarView extends FrameLayout implements WearableListView.OnCenterProximityListener {

        private final TextView title;
        private final TextView description;

        public CarView(Context context) {
            super(context);

            final View inflate = LayoutInflater.from(context).inflate(R.layout.car_card_content, null);
            title = (TextView) inflate.findViewById(R.id.card_title);
            description = (TextView) inflate.findViewById(R.id.card_description);
            addView(inflate);
        }

        @Override
        public void onCenterPosition(boolean b) {
            animate().alpha(1f).setDuration(0).start();
        }

        @Override
        public void onNonCenterPosition(boolean b) {
            animate().alpha(0.2f).setDuration(500).start();
        }
    }


    private static class CarViewHolder extends WearableListView.ViewHolder {

        private final CarView itemView;

        public CarViewHolder(CarView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    private static class CarData {

        public final String description;
        private final String title;

        public CarData(String title, String description) {
            this.description = description;
            this.title = title;
        }

    }

    private static class CarAdapter extends WearableListView.Adapter {

        final List<CarData> data;

        private final Context context;

        private CarAdapter(List<CarData> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CarViewHolder(new CarView(context));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            final CarData carData = data.get(position);
            final CarView carView = ((CarViewHolder) holder).itemView;

            carView.title.setText(carData.title);
            carView.description.setText(carData.description);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private void onClickCancel() {
        exit();
    }

    private void onClickSuccess() {
        exit();
    }

    private void exit() {
        finish();
    }
}

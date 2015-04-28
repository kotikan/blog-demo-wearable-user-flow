package com.kotikan.demo.taxitracker.activities;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.WearableListView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kotikan.demo.taxitracker.R;
import com.kotikan.demo.taxitracker.utils.AndroidWakeLock;
import com.kotikan.demo.taxitracker.utils.WakeLock;

import java.util.ArrayList;
import java.util.List;

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
                add(new CarData("Ross's car"));
                add(new CarData("Jonathan's car"));
                add(new CarData("Rob's car"));
                add(new CarData("Charlie's car"));
            }
        };
        view.setAdapter(new CarAdapter(data, this));

    }

    private static class CarView extends LinearLayout implements WearableListView.OnCenterProximityListener {

        private final TextView textView;

        public CarView(Context context) {
            super(context);
            textView = new TextView(context);
            textView.setTextColor(Color.BLACK);
            addView(textView);
        }

        @Override
        public void onCenterPosition(boolean b) {
            setAlpha(1f);
        }

        @Override
        public void onNonCenterPosition(boolean b) {
            animate().alpha(0.5f).setDuration(500).start();
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

        public CarData(String carTitle) {
            this.carTitle = carTitle;
        }

        private final String carTitle;
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
            ((CarViewHolder) holder).itemView.textView.setText(carData.carTitle);
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

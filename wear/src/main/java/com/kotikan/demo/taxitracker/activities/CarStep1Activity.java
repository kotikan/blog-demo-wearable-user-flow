package com.kotikan.demo.taxitracker.activities;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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


        final RecyclerView view = (RecyclerView) findViewById(R.id.wearable_list);
        final List<CarData> data = new ArrayList<CarData>() {
            {
                add(new CarData("£3.00", "5mins away", R.drawable.cars_01));
                add(new CarData("£3.50", "3mins away", R.drawable.cars_02));
                add(new CarData("£4.00", "2mins away", R.drawable.cars_03));
            }
        };
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setLayoutManager(layoutManager);
        view.setAdapter(new CarAdapter(data, this));
    }

    private static class CarView extends FrameLayout implements WearableListView.OnCenterProximityListener {

        private final TextView title;
        private final TextView description;
        private final ImageView image;

        public CarView(Context context) {
            super(context);

            final View inflate = LayoutInflater.from(context).inflate(R.layout.car_card_content, null);
            title = (TextView) inflate.findViewById(R.id.card_title);
            description = (TextView) inflate.findViewById(R.id.card_description);
            image = (ImageView) inflate.findViewById(R.id.card_image);
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


    private static class CarViewHolder extends RecyclerView.ViewHolder {

        private final CarView itemView;

        public CarViewHolder(CarView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    private static class CarData {

        public final String description;
        private final String title;
        private final int drawableId;

        public CarData(String title, String description, int drawableId) {
            this.description = description;
            this.title = title;
            this.drawableId = drawableId;
        }

    }

    private static class CarAdapter extends RecyclerView.Adapter {

        final List<CarData> data;

        private final Context context;

        private CarAdapter(List<CarData> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CarViewHolder(new CarView(context));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final CarData carData = data.get(position);
            final CarView carView = ((CarViewHolder) holder).itemView;

            carView.title.setText(carData.title);
            carView.description.setText(carData.description);
            carView.image.setImageResource(carData.drawableId);
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

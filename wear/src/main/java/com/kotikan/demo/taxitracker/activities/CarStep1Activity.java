package com.kotikan.demo.taxitracker.activities;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

        String userMessage = "";
        final Intent intent = getIntent();
        if (intent != null) {
            final Bundle extras = intent.getExtras();
            if (extras != null) {
                final String extraMessage = extras.getString(EXTRA_MESSAGE);
                if (extraMessage != null) {
                    userMessage = extraMessage;
                }
            }
        }
        final String userMessagePassedThrough = userMessage;

        final Vibrator systemService = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        systemService.vibrate(75l);


        final RecyclerView view = (RecyclerView) findViewById(R.id.wearable_list);
        final List<CarData> data = new ArrayList<CarData>() {
            {
                add(CarData.paddingRow());
                add(new CarData("£3.00", "5mins away", R.drawable.cars_01));
                add(new CarData("£3.50", "3mins away", R.drawable.cars_02));
                add(new CarData("£4.00", "2mins away", R.drawable.cars_03));
                add(CarData.paddingRow());
            }
        };
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setLayoutManager(layoutManager);
        view.setAdapter(new CarAdapter(data, this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CarStep1Activity carStep1Activity = CarStep1Activity.this;
                final CarView castView = (CarView) view;

                final String message = userMessagePassedThrough + "\n" + castView.description.getText().toString() + " @" + castView.title.getText().toString();
                CountdownAcceptActivity.startWithData(carStep1Activity, message);
                finish();
            }
        }));
    }

    private static class CarView extends FrameLayout {

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
    }

    private static class CarViewHolder extends RecyclerView.ViewHolder {

        private final CarView itemView;

        public CarViewHolder(CarView itemView, View.OnClickListener listener) {
            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(listener);
        }
    }

    private static class CarData {

        public final boolean isHeader;
        public final String description;
        public final String title;
        public final int drawableId;

        static CarData paddingRow() {
            return new CarData(null, null, 0, true);
        }

        public CarData(String title, String description, int drawableId) {
            this(title, description, drawableId, false);
        }

        private CarData(String title, String description, int drawableId, boolean isHeader) {
            this.description = description;
            this.title = title;
            this.drawableId = drawableId;
            this.isHeader = isHeader;
        }

    }

    private static class CarAdapter extends RecyclerView.Adapter {

        final List<CarData> data;

        private final Context context;
        private final View.OnClickListener listener;

        private CarAdapter(List<CarData> data, Context context, View.OnClickListener listener) {
            this.data = data;
            this.context = context;
            this.listener = listener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                final FrameLayout viewGroup = new FrameLayout(context);
                final float pxFor50Dp = context.getResources().getDimension(R.dimen.px_for_50_dp);
                viewGroup.addView(new View(context), new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) pxFor50Dp));
                return new RecyclerView.ViewHolder(viewGroup){};
            } else  {
                return new CarViewHolder(new CarView(context), listener);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final CarData carData = data.get(position);
            if (!carData.isHeader){
                final CarView carView = ((CarViewHolder) holder).itemView;

                carView.title.setText(carData.title);
                carView.description.setText(carData.description);
                carView.image.setImageResource(carData.drawableId);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        @Override
        public int getItemViewType(int position) {
            return data.get(position).isHeader ? 0 : 1;
        }
    }
}

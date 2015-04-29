package com.kotikan.demo.taxitracker.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.kotikan.demo.taxitracker.R;
import com.kotikan.demo.taxitracker.utils.Extras;

public class CarNotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.car_notification);

        final ImageView imageView = (ImageView) findViewById(R.id.message_image);
        final TextView title      = (TextView) findViewById(R.id.message_title);
        final TextView content    = (TextView) findViewById(R.id.message_content);

        final Bundle extras = getIntent().getExtras();
        title.setText(extras.getString(Extras.EXTRA_CAR_PRICE, ""));
        content.setText(extras.getString(Extras.EXTRA_CAR_ARRIVES_IN, ""));
        imageView.setImageResource(extras.getInt(Extras.EXTRA_CAR_DRAWABLE_ID, -1));

    }
}

package com.kotikan.demo.taxitracker.view;

import com.kotikan.demo.taxitracker.R;

import java.util.HashMap;

public class AndroidCarView implements CarView {

    @Override
    public String getMessageText(int viewId) {
        final HashMap<Integer, String> map = new HashMap<>();
        map.put(R.id.radio_airport, "Car to airport in %ss");
        map.put(R.id.radio_meeting, "Car to meeting in %ss");
        map.put(R.id.radio_haircut, "Car to haircut in %ss");
        return map.get(viewId);
    }

    @Override
    public String passThroughMessageText(int viewId) {
        final HashMap<Integer, String> map = new HashMap<>();
        map.put(R.id.radio_airport, "Book car to airport");
        map.put(R.id.radio_meeting, "Book car to meeting");
        map.put(R.id.radio_haircut, "Book car to hairdressers");
        return map.get(viewId);    }

    @Override
    public String getCarArrivesIn() {
        return "30";
    }

}

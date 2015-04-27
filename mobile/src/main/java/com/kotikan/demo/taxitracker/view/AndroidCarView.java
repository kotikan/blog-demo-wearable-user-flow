package com.kotikan.demo.taxitracker.view;

import com.kotikan.demo.taxitracker.R;

import java.util.HashMap;
import java.util.Map;

public class AndroidCarView implements CarView {

    @Override
    public String getMessageText(int viewId) {
        return loadMessages().get(viewId);

    }

    private Map<Integer, String> loadMessages() {
        final HashMap<Integer, String> map = new HashMap<>();
        map.put(R.id.radio_airport, "Car to airport in %ss");
        map.put(R.id.radio_meeting, "Car to meeting in %ss");
        map.put(R.id.radio_haircut, "Car to haircut in %ss");
        return map;
    }

    @Override
    public String getCarArrivesIn() {
        return "30";
    }

}

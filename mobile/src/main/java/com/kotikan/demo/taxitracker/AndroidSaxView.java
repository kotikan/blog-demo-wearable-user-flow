package com.kotikan.demo.taxitracker;

import android.util.SparseArray;

import com.kotikan.demo.taxitracker.view.SaxView;

public class AndroidSaxView implements SaxView {
    @Override
    public String getMessageText(int viewId) {
        return new SparseArray<String>() {
            {
                put(R.id.sax_6pm, "The Jazz Bar");
                put(R.id.sax_7pm, "The Piano Bar");
                put(R.id.sax_8pm, "The Garage");
            }
        }.get(viewId);
    }
}

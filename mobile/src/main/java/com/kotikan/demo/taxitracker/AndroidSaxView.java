package com.kotikan.demo.taxitracker;

import android.util.SparseArray;

import com.kotikan.demo.taxitracker.view.SaxView;

public class AndroidSaxView implements SaxView {
    @Override
    public String getMessageText(int viewId) {
        return new SparseArray<String>() {
            {
                put(R.id.sax_6pm, "Play music at 6pm");
                put(R.id.sax_7pm, "Play music at 7pm");
                put(R.id.sax_8pm, "Play music at 8pm");
            }
        }.get(viewId);
    }
}

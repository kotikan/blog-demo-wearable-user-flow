package com.kotikan.demo.taxitracker.view;

public interface CarView {
    String getMessageText(int viewId);

    String passThroughMessageText(int viewId);

    String getCarArrivesIn();
}

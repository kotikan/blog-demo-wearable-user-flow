package com.kotikan.demo.taxitracker.utils;

public class RandomGenerator {
    public String newPrice(int min, int max) {
        final double dPounds = Math.random() * (max - min);
        int pounds = min + (int) dPounds;

        final double dPennies = Math.random() * 99;
        int pennies = (int) dPennies;

        String sPennies = String.valueOf(pennies);
        while (sPennies.length() < 2) {
            sPennies = "0" + sPennies;
        }
        return "£" + String.valueOf(pounds) + "." + sPennies;
    }

    public String newTime() {
        final double dMins = Math.random() * 4;
        int mins = 1 + (int) dMins;

        return String.valueOf(mins) + "mins";
    }
}

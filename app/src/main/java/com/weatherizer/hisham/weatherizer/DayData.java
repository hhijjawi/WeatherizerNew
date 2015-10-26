package com.weatherizer.hisham.weatherizer;

/**
 * Created by Hisham on 10/26/2015.
 */
public class DayData {
    private int minTemp;
    private int maxTemp;
    private int chanceOfRain;
    private String dayOfWeek;
    private String iconURL;

    public String getIconURL() {
        return iconURL;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public int getChanceOfRain() {
        return chanceOfRain;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public DayData(int minTemp, int maxTemp, int chanceOfRain, String dayOfWeek, String iconURL) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.chanceOfRain = chanceOfRain;
        this.dayOfWeek = dayOfWeek;
        this.iconURL = iconURL;
    }
}

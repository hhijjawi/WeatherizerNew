package com.weatherizer.hisham.weatherizer;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Hisham on 10/25/2015.
 */
public class LocationUpdater implements LocationListener {
    private final Activity mainActivity;

    public LocationUpdater(Activity mainActivity) {
        this.mainActivity=mainActivity;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(MainActivity.mZipCode==0){
            //If we don't hvae a zip pass in the location
            new MainActivity.getWeatherData(mainActivity).execute(location.getLatitude(),location.getLongitude());
        }
        else{
            //if I don't have the zip, find the location of the zip  in the Asynctask.
            // Passing in null tells me I'm missing something in the AsyncTask
            Double latitude=null;
            Double lon=null;
            new MainActivity.getWeatherData(mainActivity).execute(latitude,lon);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

package com.example.interactive_newspaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class GPSLocation implements LocationListener {
    private double longit,latit;

    Context appContext = MainActivity.getGpsContext();
    SharedPreferences.Editor prefs = appContext.getSharedPreferences("Interactive_Newspaper_prefs", Context.MODE_PRIVATE).edit();


    @Override

    public void onLocationChanged(Location location) {


        if(location.getAccuracy() > 10) {
            longit = location.getLongitude();
            latit = location.getLatitude();
        }
        if(location.getAccuracy() <= 10){
            // add longit and latit to sharedPrefs
            prefs.putFloat("LONGITUDE", (float)longit);
            prefs.putFloat("LATITUDE", (float)latit);
            prefs.apply();
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

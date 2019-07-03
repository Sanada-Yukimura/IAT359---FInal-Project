package com.example.interactive_newspaper;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

public class LightSensorTheme extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor luxSensor;

    public LightSensorTheme() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        luxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(luxSensor != null) {
            sensorManager.registerListener(this, luxSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }


    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int typeSensor = event.sensor.getType();
        if(typeSensor == Sensor.TYPE_LIGHT) {
            try {
                SharedPreferences.Editor editThis = getSharedPreferences("INTERACTIVE_NEWSPAPER", MODE_PRIVATE).edit();
                if (event.values[0] != 10){


                    editThis.putBoolean("DARK_THEME", true);
                }
                else{
                    editThis.putBoolean("DARK_THEME", false);
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

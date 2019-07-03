package com.example.interactive_newspaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.interactive_newspaper_SQL.SQLDatabaseAdapter;
import com.example.interactive_newspaper_SQL.SQLDatabaseHelper;
import com.example.interactive_newspaper_SQL.SQLMyDatabase;

import java.sql.SQLData;
import java.util.ArrayList;

public class Bookmarks extends AppCompatActivity implements SensorEventListener {

    private RecyclerView bookmarkRecycler;
    private RecyclerView.LayoutManager layoutRecycler;
    SQLMyDatabase db;
    SQLDatabaseAdapter thisAdapter;
    SQLDatabaseHelper helper;
    private SensorManager sensorManager;
    Context context;
    private Sensor luxSensor;
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        luxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        setContentView(R.layout.activity_bookmarks);
        bookmarkRecycler = findViewById(R.id.bookmarkRecycler);
        layoutRecycler = new LinearLayoutManager(this);
        bookmarkRecycler.setLayoutManager(layoutRecycler);

        db = new SQLMyDatabase(this);
        helper = new SQLDatabaseHelper(this);

        Cursor c = (Cursor) db.getData();
        int indexingOne = c.getColumnIndex("Title");

        ArrayList<String> thisList = new ArrayList<>();
        c.moveToFirst();
        int idNumber = 1;
        while(!c.isAfterLast()){
            String title = c.getString(indexingOne);
            String appendText = title;
            idNumber++;
            thisList.add(appendText);
            c.moveToNext();
        }
        idNumber = 1;

        thisAdapter = new SQLDatabaseAdapter(thisList, this);
        bookmarkRecycler.setAdapter(thisAdapter);




    }

    @Override
    protected void onResume() {
        super.onResume();
        if(luxSensor != null) {
            sensorManager.registerListener(this, luxSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int typeSensor = event.sensor.getType();
        if(typeSensor == Sensor.TYPE_LIGHT) {
            try {
                Log.d("LUX", Float.toString(event.values[0]));
                SharedPreferences.Editor editThis = getSharedPreferences("INTERACTIVE_NEWSPAPER", MODE_PRIVATE).edit();
                if (event.values[0] <= 8){
                    // If the light sensor detects darkness, then set the theme to Night mode

                    editThis.putBoolean("DARK_THEME", true);
                    editThis.apply();
                    Log.d("LUX", "edit this successfully: true");
                    SharedPreferences prefs = getSharedPreferences("INTERACTIVE_NEWSPAPER", MODE_PRIVATE);
                    Boolean darkThemeOn = prefs.getBoolean("DARK_THEME", false);
                    Log.d("NOX", Boolean.toString(darkThemeOn));
                    if(darkThemeOn){
                        SetTheme('d');
                    }
                }
                else{
                    // If the light sensor detects daylight, then set the theme to light mode
                    editThis.putBoolean("DARK_THEME", false);
                    editThis.apply();
                    Log.d("LUX", "edit this successfully: false");
                    SharedPreferences prefs = getSharedPreferences("INTERACTIVE_NEWSPAPER", MODE_PRIVATE);
                    Boolean darkThemeOn = prefs.getBoolean("DARK_THEME", false);
                    Log.d("NOX", Boolean.toString(darkThemeOn));
                    if(!darkThemeOn){
                        SetTheme('l');
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void SetTheme(char t) {
        if (t == 'd') {
            constraintLayout.setBackgroundResource(R.color.darkBack);

        }
        if (t == 'l') {
            constraintLayout.setBackgroundResource(R.color.lightBack);
        }
    }


}

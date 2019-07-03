package com.example.interactive_newspaper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.interactive_newspaper_SQL.SQLMyDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener, SensorEventListener {

    private SensorManager sensorManager;
    Context context;
    private Sensor luxSensor;
    ConstraintLayout constraintLayout;
    Button localNews, worldNews, sports, weather, bookmarks;
    TextView currDate, location;
    String todaysDate;
    LocationManager locationManager;
    boolean networkOn;
    Location locationVal;
    String city, province, country;
    private Handler myHandler = new Handler();
    private static Context gpsContext;
    private String provider;
    SharedPreferences.Editor editPrefs;
    boolean firstStart = true;


    private double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editPrefs = getSharedPreferences("INTERACTIVE_NEWSPAPER", MODE_PRIVATE).edit();
        localNews = findViewById(R.id.localNews);
        worldNews = findViewById(R.id.worldNews);
        sports = findViewById(R.id.sports);
        bookmarks = findViewById(R.id.bookmarks);
        localNews.setOnClickListener(this);
        worldNews.setOnClickListener(this);
        sports.setOnClickListener(this);
        bookmarks.setOnClickListener(this);
        currDate = findViewById(R.id.currentDate);
        location = findViewById(R.id.location);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        luxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);
        }
        gpsContext = getApplicationContext();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        todaysDate = sdf.format(today);
        currDate.setText(todaysDate);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);

        }

        try{
            locationManager.requestLocationUpdates(provider, 500, 1, this);
        }
        catch(Exception e){
            firstStart = false;
        }
        if(provider != null) {
            locationManager.requestLocationUpdates(provider, 500, 1, this);
        }
        SharedPreferences prefs = getSharedPreferences("INTERACTIVE_NEWSPAPER", MODE_PRIVATE);
        // Retrieves latitude and longitude values from SharedPreferences
        double lat = prefs.getFloat("LATITUDE", (float)0.0);
        double longit = prefs.getFloat("LONGITUDE", (float)0.0);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            // A hashmap containing each State and Province and their initials.
            Map<String, String> adminAreas = new HashMap<>();
            adminAreas.put("Alabama","AL");
            adminAreas.put("Alaska","AK");
            adminAreas.put("Alberta","AB");
            adminAreas.put("American Samoa","AS");
            adminAreas.put("Arizona","AZ");
            adminAreas.put("Arkansas","AR");
            adminAreas.put("Armed Forces (AE)","AE");
            adminAreas.put("Armed Forces Americas","AA");
            adminAreas.put("Armed Forces Pacific","AP");
            adminAreas.put("British Columbia","BC");
            adminAreas.put("California","CA");
            adminAreas.put("Colorado","CO");
            adminAreas.put("Connecticut","CT");
            adminAreas.put("Delaware","DE");
            adminAreas.put("District Of Columbia","DC");
            adminAreas.put("Florida","FL");
            adminAreas.put("Georgia","GA");
            adminAreas.put("Guam","GU");
            adminAreas.put("Hawaii","HI");
            adminAreas.put("Idaho","ID");
            adminAreas.put("Illinois","IL");
            adminAreas.put("Indiana","IN");
            adminAreas.put("Iowa","IA");
            adminAreas.put("Kansas","KS");
            adminAreas.put("Kentucky","KY");
            adminAreas.put("Louisiana","LA");
            adminAreas.put("Maine","ME");
            adminAreas.put("Manitoba","MB");
            adminAreas.put("Maryland","MD");
            adminAreas.put("Massachusetts","MA");
            adminAreas.put("Michigan","MI");
            adminAreas.put("Minnesota","MN");
            adminAreas.put("Mississippi","MS");
            adminAreas.put("Missouri","MO");
            adminAreas.put("Montana","MT");
            adminAreas.put("Nebraska","NE");
            adminAreas.put("Nevada","NV");
            adminAreas.put("New Brunswick","NB");
            adminAreas.put("New Hampshire","NH");
            adminAreas.put("New Jersey","NJ");
            adminAreas.put("New Mexico","NM");
            adminAreas.put("New York","NY");
            adminAreas.put("Newfoundland","NF");
            adminAreas.put("North Carolina","NC");
            adminAreas.put("North Dakota","ND");
            adminAreas.put("Northwest Territories","NT");
            adminAreas.put("Nova Scotia","NS");
            adminAreas.put("Nunavut","NU");
            adminAreas.put("Ohio","OH");
            adminAreas.put("Oklahoma","OK");
            adminAreas.put("Ontario","ON");
            adminAreas.put("Oregon","OR");
            adminAreas.put("Pennsylvania","PA");
            adminAreas.put("Prince Edward Island","PE");
            adminAreas.put("Puerto Rico","PR");
            adminAreas.put("Quebec","PQ");
            adminAreas.put("Rhode Island","RI");
            adminAreas.put("Saskatchewan","SK");
            adminAreas.put("South Carolina","SC");
            adminAreas.put("South Dakota","SD");
            adminAreas.put("Tennessee","TN");
            adminAreas.put("Texas","TX");
            adminAreas.put("Utah","UT");
            adminAreas.put("Vermont","VT");
            adminAreas.put("Virgin Islands","VI");
            adminAreas.put("Virginia","VA");
            adminAreas.put("Washington","WA");
            adminAreas.put("West Virginia","WV");
            adminAreas.put("Wisconsin","WI");
            adminAreas.put("Wyoming","WY");
            adminAreas.put("Yukon Territory","YT");

            // Using the latitude and longitude, get the current location's city, state or province and country.
            // This then sets the text onto the location value.
            List<Address> addresses = geocoder.getFromLocation(lat,longit,1);
            if(addresses.size() > 0) {

                Address currentLocation = addresses.get(0);
                city = currentLocation.getLocality();
                province = adminAreas.get(currentLocation.getAdminArea());
                editPrefs.putString("ADMIN_AREA", province);
                editPrefs.apply();
                country = currentLocation.getCountryName();
                String locationFormat = city + ", " + province+ ", "  + country;
                location.setText(locationFormat);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(luxSensor != null) {
            sensorManager.registerListener(this, luxSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        locationManager.removeUpdates(this);
        super.onPause();


    }

    @Override
    protected void onDestroy() {
        SQLMyDatabase db = new SQLMyDatabase(this);
        db.deleteTable();
        super.onDestroy();
    }

    // Clicking the button will initialize the activity corresponding to the button text.
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.localNews:
                Intent localNewsIntent = new Intent(this, LocalNews.class);
                startActivity(localNewsIntent);
                break;

            case R.id.worldNews:
                Intent worldNewsIntent = new Intent(this, WorldNews.class);
                startActivity(worldNewsIntent);
                break;

            case R.id.sports:
                Intent sportsIntent = new Intent(this, Sports.class);
                startActivity(sportsIntent);
                break;

            case R.id.bookmarks:
                Intent bookmarkIntent = new Intent(this, Bookmarks.class);
                startActivity(bookmarkIntent);
                break;
        }

    }

    public static Context getGpsContext(){
        return gpsContext;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        editPrefs.putFloat("LATITUDE", (float)latitude);
        editPrefs.putFloat("LONGITUDE", (float)longitude);
        editPrefs.apply();

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
                Log.d("error", "something has happened");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // using the values in color.xml, change the theme of the app according to dark or light
    private void SetTheme(char t){
        if(t == 'd') {
            constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBack));
            localNews.setTextColor(ContextCompat.getColor(this, R.color.darkSecondaryFont));
            localNews.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBackSecondary));
            worldNews.setTextColor(ContextCompat.getColor(this, R.color.darkSecondaryFont));
            worldNews.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBackSecondary));
            sports.setTextColor(ContextCompat.getColor(this, R.color.darkSecondaryFont));
            sports.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBackSecondary));
            bookmarks.setTextColor(ContextCompat.getColor(this, R.color.darkSecondaryFont));
            bookmarks.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBackSecondary));
            weather.setTextColor(ContextCompat.getColor(this, R.color.darkSecondaryFont));
            weather.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBackSecondary));
            currDate.setTextColor(ContextCompat.getColor(this, R.color.darkFont));
            location.setTextColor(ContextCompat.getColor(this, R.color.darkFont));
        }
        if(t == 'l'){
            constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBack));
            localNews.setTextColor(ContextCompat.getColor(this, R.color.lightFont));
            localNews.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBackSecondary));
            worldNews.setTextColor(ContextCompat.getColor(this, R.color.lightFont));
            worldNews.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBackSecondary));
            sports.setTextColor(ContextCompat.getColor(this, R.color.lightFont));
            sports.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBackSecondary));
            bookmarks.setTextColor(ContextCompat.getColor(this, R.color.lightFont));
            bookmarks.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBackSecondary));
            weather.setTextColor(ContextCompat.getColor(this, R.color.lightFont));
            weather.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBackSecondary));
            currDate.setTextColor(ContextCompat.getColor(this, R.color.lightFont));
            location.setTextColor(ContextCompat.getColor(this, R.color.lightFont));
        }

    }




}

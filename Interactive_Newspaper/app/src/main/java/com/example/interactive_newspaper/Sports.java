package com.example.interactive_newspaper;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

import com.example.interactive_newspaper_NewsFeed.NewsFeedListAdapter;
import com.example.interactive_newspaper_NewsFeed.NewsFeedModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Sports extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    Context context;
    private Sensor luxSensor;
    ConstraintLayout constraintLayout;
    private RecyclerView myRecyclerView;
    private RecyclerView.LayoutManager myLayoutManager;
    private TextView newsSource;
    private String newsURL;
    private String myFeedTitle;
    private String myFeedDescrip;
    private List<NewsFeedModel> myFeedList;
    RecyclerView.Adapter adapter;
    Parcelable myListState;
    List<NewsFeedModel> items = new ArrayList<>();
    public boolean darkMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        luxSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        newsSource = findViewById(R.id.newsSource);
        myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);






        // enter RSS feed URL below:
        newsURL = "https://rss.cbc.ca/lineup/sports.xml";

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Sports.FetchNewsFeed().execute((Void)null);


    }

    @Override
    protected void onResume() {
        super.onResume();
        myLayoutManager.onRestoreInstanceState(myListState);
        SharedPreferences prefs = getSharedPreferences("INTERACTIVE_NEWSPAPER", MODE_PRIVATE);
        if(prefs.getBoolean("DARK_THEME", false)){
            darkMode = true;
            newsSource.setTextColor(ContextCompat.getColor(this, R.color.darkFont));

        }
        else{
            darkMode = false;
        }
        if(luxSensor != null) {
            sensorManager.registerListener(this, luxSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myListState = myLayoutManager.onSaveInstanceState();
        outState.putParcelable("LIST_SAVE_STATE", myListState);

    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState !=null){
            myListState = savedInstanceState.getParcelable("LIST_SAVE_STATE");
        }
    }
    public List<NewsFeedModel> parseFeed (InputStream inputStream) throws XmlPullParserException, IOException {
        String title = null;
        String link = null;
        String descrip = null;
        boolean isItem = false;


        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }


                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = "\n" + result + "\n";
                } else if (name.equalsIgnoreCase("link")) {

                    link = "\n" + result + "\n";
                } else if (name.equalsIgnoreCase("description")) {
                    if(result.contains("<p>")) {
                        String extract = result.substring(result.indexOf("<p>") + 3, result.indexOf("</p>"));

                        descrip = "\n" + extract + "\n";
                    }
                    else{
                        descrip = "\n" + result + "\n";
                    }
                }

                if (title != null && link != null && descrip != null) {
                    if(isItem) {
                        NewsFeedModel item = new NewsFeedModel(title, link, descrip);
                        items.add(item);
                    }
                    else {
                        myFeedDescrip = descrip;
                        myFeedTitle = title;
                        newsURL = link;
                    }
                    title = null;
                    link = null;
                    descrip = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
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


    private class FetchNewsFeed extends AsyncTask<Void, Void, Boolean> {
        private String newsFeedUrl;

        protected void onPreExecute(){
            newsSource.setText("News Source: " +newsURL);
            newsFeedUrl = newsURL;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(TextUtils.isEmpty(newsFeedUrl)){
                return false;
            }
            try{
                URL url = new URL(newsFeedUrl);
                InputStream inputStream = url.openConnection().getInputStream();
                myFeedList = parseFeed(inputStream);
                return true;


            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean success) {

            if (success) {
                newsSource.setText("Feed Link: " + newsFeedUrl);
                // Fill RecyclerView
                adapter = new NewsFeedListAdapter((myFeedList));
                myRecyclerView.setAdapter(adapter);
            }
        }
    }
    private void SetTheme(char t) {
        if (t == 'd') {
            constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.darkBack));
            newsSource.setTextColor(ContextCompat.getColor(this, R.color.darkFont));

        }
        if (t == 'l') {
            constraintLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBack));
            newsSource.setTextColor(ContextCompat.getColor(this, R.color.lightFont));

        }
    }

}

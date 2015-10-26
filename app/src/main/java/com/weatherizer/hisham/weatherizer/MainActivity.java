package com.weatherizer.hisham.weatherizer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {
    static ListView mMultiDayForecastListView;
    static Button mRadarButton;
    static Button mWeatherLadyButton;
    static Integer mZipCode = 0; //Default Value to see if user has modified the zip code
    static Boolean isFirstTime = true;
    static String units = "fahrenheit";
    static Button settingsButton;
    static int numDays = 9;
    static String forecastToBeRead;
    static SharedPreferences.Editor sharedPreferencesEditor;

     SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         sharedPreferences=getPreferences(MODE_PRIVATE);
        int restoredZip = sharedPreferences.getInt("zip", 0);
        Log.d("restored",""+restoredZip);
        if(restoredZip!=0){
            mZipCode=restoredZip;
        }
        int restoredNumDays=sharedPreferences.getInt("numDays",0);
        if(restoredNumDays!=0){
            numDays=restoredNumDays;
        }
        mMultiDayForecastListView = (ListView) findViewById(R.id.listView);
        mRadarButton = (Button) findViewById(R.id.radarButton);
        mRadarButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this, RadarActivity.class);
                startActivity(i);
            }
        });
        mWeatherLadyButton = (Button) findViewById(R.id.weatherLadyButton);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        LocationListener lUpdater = new LocationUpdater(MainActivity.this);
        //This is only here for the first run of the program or rotation Changes; I don't rely on getLastKnownLocation,
        // only an interm solution till I update location the first time
        //I know I shouldn't call callbacks myself. I don't know how to store my ListView as a Serializable or Parcelable. so I didn't know how to store it when the activity was destroyed.
        lUpdater.onLocationChanged(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
       /*if (savedInstanceState != null) {

            new getWeatherData(MainActivity.this).execute( locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        ProgressDialog.show(MainActivity.this,"Wait","Wait");}*/
        //Only start requesting location updates once
        if (isFirstTime) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, new LocationUpdater(MainActivity.this));
        }
        isFirstTime = false;
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });
        final TextToSpeech tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
            }
        }
        );
        //I like the british accent and the cadence kind of slow
        tts.setLanguage(Locale.UK);
        tts.setSpeechRate((float) 0.80);
        mWeatherLadyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                char[] temp=forecastToBeRead.toCharArray();
                //Remove F's C's at the end of each number for smooth reading
                for(int i=2; i<temp.length;i++){
                    if(Character.isDigit(temp[i-2])&&Character.isDigit(temp[i-1])&&(temp[i]=='F'||temp[i]=='C')){
                        temp[i]=' ';
                    }
                }
                forecastToBeRead=new String(temp);
                forecastToBeRead = forecastToBeRead.replaceAll(" N ", "North");
                forecastToBeRead = forecastToBeRead.replaceAll(" E ", "East");
                forecastToBeRead = forecastToBeRead.replaceAll(" W ", "West");
                forecastToBeRead = forecastToBeRead.replaceAll(" S ", "South");

                forecastToBeRead = forecastToBeRead.replaceAll(" NE ", "Northeast");
                forecastToBeRead = forecastToBeRead.replaceAll(" SE ", "Southeast");
                forecastToBeRead = forecastToBeRead.replaceAll(" NW ", "Northwest");
                forecastToBeRead = forecastToBeRead.replaceAll(" SW ", "Southwest");
                forecastToBeRead = forecastToBeRead.replaceAll(" SSE ", "South to Southeast");
                forecastToBeRead = forecastToBeRead.replaceAll(" SSW ", "South to Southwest");
                forecastToBeRead = forecastToBeRead.replaceAll(" NNE ", "North to Northeast");
                forecastToBeRead = forecastToBeRead.replaceAll(" NNW ", "North to Northwest");
                forecastToBeRead = forecastToBeRead.replaceAll(" WSW ", "West to Southwest");

                tts.speak(forecastToBeRead, TextToSpeech.QUEUE_ADD, null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        Log.d("called","calledOnPause");
        sharedPreferencesEditor=getPreferences(MODE_PRIVATE).edit();
        sharedPreferencesEditor.putInt("zip",mZipCode);
        sharedPreferencesEditor.putInt("numDays", numDays);
        super.onPause();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("ListView", mMultiDayForecastListView.onSaveInstanceState());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onDestroy() {
        sharedPreferencesEditor=getPreferences(MODE_PRIVATE).edit();
        sharedPreferencesEditor.putInt("zip",mZipCode);
        sharedPreferencesEditor.putInt("numDays",numDays);
        Log.d("called","calledOnDestroy");
        super.onDestroy();
    }



    static class getWeatherData extends AsyncTask {
        ProgressDialog pleaseWait;
        static Activity mainActivity;
        JSONObject tenDayForecastAsJson = null;

        public getWeatherData(Activity mainActivity) {
            this.mainActivity = mainActivity;
            this.pleaseWait = new ProgressDialog(mainActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pleaseWait.setMessage("Please Wait For Your Weather.");
            if (!mainActivity.isFinishing()) {
                pleaseWait.show();            }

        }

        @Override
        protected Object doInBackground(Object[] params) {

            //Get Weather Data
            Double latitude = (Double) params[0];
            Double longitude = (Double) params[1];
            String city = null;
            Log.d("Latitude", "" + latitude);
            if (latitude == null) {
                try {
                    JSONObject geoLookUp = new JSONObject(Ion.with(mainActivity)
                            .load("http://api.wunderground.com/api/4b731cd2157e63e2/geolookup/q/" + mZipCode.toString() + ".json"
                            )
                            .asJsonObject().get().toString());
                    // JSONObject geoLookUp = JsonReader.readJsonFromUrl("http://api.wunderground.com/api/4b731cd2157e63e2/geolookup/q/" + mZipCode.toString() + ".json");
                    latitude = geoLookUp.getJSONObject("location").getDouble("lat");
                    longitude = geoLookUp.getJSONObject("location").getDouble("lon");
                    city = findCityName(latitude, longitude);
                    //My JSONReader that I got from stack Started giving me issues (kept returning a string, so I switched to Ion when we learned about it
                    while (!(tenDayForecastAsJson instanceof JSONObject)) {
                        //JSONParser parser_obj = new JSONParser();
                        Log.d("here","here");
                        tenDayForecastAsJson = new JSONObject(Ion.with(mainActivity)
                                .load("http://api.wunderground.com/api/4b731cd2157e63e2/forecast10day/q/" + city + ".json")
                                .asJsonObject().get().toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            } else {
                try {

                    city = findCityName((Double) params[0], (Double) params[1]);
                    tenDayForecastAsJson = new JSONObject(Ion.with(mainActivity)
                            .load("http://api.wunderground.com/api/4b731cd2157e63e2/forecast10day/q/" + city + ".json")
                            .asJsonObject().get().toString());
                    while (!(tenDayForecastAsJson instanceof JSONObject)) {
                        //JSONParser parser_obj = new JSONParser();
                        Log.d("here2", "here2");
                        tenDayForecastAsJson = new JSONObject(Ion.with(mainActivity)
                                .load("http://api.wunderground.com/api/4b731cd2157e63e2/forecast10day/q/" + city + ".json")
                                .asJsonObject().get().toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            while (tenDayForecastAsJson == null) {
                try {
                    Log.d("here3","here3");
                    tenDayForecastAsJson = new JSONObject(Ion.with(mainActivity)
                            .load("http://api.wunderground.com/api/4b731cd2157e63e2/forecast10day/q/" + city + ".json")
                            .asJsonObject().get().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return tenDayForecastAsJson;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            MainActivity.forecastToBeRead = "";
            if(MainActivity.units=="celsius"){
            for (int i = 0; i < (MainActivity.numDays * 2); i++) {
                try {
                    MainActivity.forecastToBeRead = MainActivity.forecastToBeRead + tenDayForecastAsJson.getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(i).getString("title") + tenDayForecastAsJson.getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(i).getString("fcttext_metric") + " ";
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }}
            else  {
                for (int i = 0; i < (MainActivity.numDays * 2); i++) {
                    try {
                        MainActivity.forecastToBeRead = MainActivity.forecastToBeRead + tenDayForecastAsJson.getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(i).getString("title") + tenDayForecastAsJson.getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(i).getString("fcttext") + " ";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }}
            //Inflate the view for the weather Data USing accquired JSON Object
            JSONObject tenDayForecastAsJson = (JSONObject) o;
            ArrayList<DayData> tenDayForecastAsArrList = new ArrayList<DayData>();
            Log.d("tenDay", tenDayForecastAsJson.toString());
            try {
                tenDayForecastAsArrList = getTenDayAsArrayList(tenDayForecastAsJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            MainActivity.mMultiDayForecastListView.setAdapter(new ForecastListAdapter(mainActivity, 0, tenDayForecastAsArrList));
            pleaseWait.dismiss();

        }

        public static String findCityName(Double latitude, Double longitude) throws IOException, JSONException, ExecutionException, InterruptedException {
            JSONObject locDetails = new JSONObject(Ion.with(mainActivity)
                    .load("http://api.wunderground.com/api/4b731cd2157e63e2/geolookup/q/" + latitude + "," + longitude + ".json")
                    .asJsonObject().get().toString());
            String cityWithWhiteSpace = locDetails.getJSONObject("location").getString("state") +"/"+locDetails.getJSONObject("location").getString("city");
            String cityWithoutWhiteSpace = cityWithWhiteSpace.replace(" ", "_").toLowerCase();
RadarActivity.city=cityWithoutWhiteSpace;
            return cityWithoutWhiteSpace;
        }

        public static ArrayList<DayData> getTenDayAsArrayList(JSONObject tenDayAsJson) throws JSONException {
            ArrayList<DayData> tenDayArrayList = new ArrayList<DayData>();
            for (int i = 0; i < numDays; i++) {
                int minTemp = tenDayAsJson.getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(i).getJSONObject("low").getInt(MainActivity.units);
                int maxTemp = tenDayAsJson.getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(i).getJSONObject("high").getInt(MainActivity.units);
                int chanceOfRain = tenDayAsJson.getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(i).getInt("pop");
                String iconURL = tenDayAsJson.getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(i).getString("icon_url");
                String dayOfWeek = tenDayAsJson.getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(i).getJSONObject("date").getString("weekday");
                DayData dData = new DayData(minTemp, maxTemp, chanceOfRain, dayOfWeek, iconURL);
                tenDayArrayList.add(dData);
            }
            return tenDayArrayList;
        }

    }
}

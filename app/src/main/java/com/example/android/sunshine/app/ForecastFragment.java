package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            String postalCode="33626";
            new FetchWeatherTask().execute(postalCode);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        String[] forecastArray = new String[]{"Today - Sunny - 86/83",
                "Tomorrow - Foggy - 70/46",
                "Weds - Cloudy - 72/63",
                "Thurs - Rainy - 64/51",
                "Fri - Foggy - 70/46",
                "Sat - Sunny - 76/68",
                "Sun - Sunny - 20/7"};

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));

        ArrayAdapter<String> mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast,
                                                                        R.id.list_item_forecast_textView,weekForecast );

        ListView listView =  (ListView) rootView.findViewById(R.id.listView_forecast);
        listView.setAdapter(mForecastAdapter);
      //  new FetchWeatherTask().execute( );
        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
        public Void doInBackground(String... postalCode){

            if(postalCode.length == 0)
                return null;

            String format = "json";
            String units = "metric";
            int numDays = 7;




            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJSONString = null;

            try{


                final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                                    .appendQueryParameter(QUERY_PARAM,postalCode[0])
                                    .appendQueryParameter(FORMAT_PARAM,format)
                                    .appendQueryParameter(UNITS_PARAM,units)
                                    .appendQueryParameter(DAYS_PARAM,Integer.toString(numDays)).build();

                /* //mndivya
                Uri.Builder builder = new Uri.Builder();


                builder.scheme("http")
                        .authority("api.openweathermap.org")
                        .appendPath("data")
                        .appendPath("2.5")
                        .appendPath("forecast")
                        .appendPath("daily")
                        .appendQueryParameter("q", postalCode[0])
                        .appendQueryParameter("mode", "json")
                        .appendQueryParameter("units", "metric")
                        .appendQueryParameter("cnt","7");


                //Constructs the URL for the openWeatherMap query
                // possible parameters are available at OWM's forecast API page at
                //http://openweathermap.org/API#forecast
                String myUrl = builder.build().toString();*/

                String myUri = builtUri.toString();

                Log.v("FetchWeatherTask",myUri);
                URL url = new URL(myUri);
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine())!=  null)
                {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + '\n');
                }

                if(buffer.length() == 0){
                    return null;
                }

                forecastJSONString = buffer.toString();
                Log.v("ForecastJSONString:",forecastJSONString);
            }catch(IOException e) {
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                Log.e(LOG_TAG, "Error",e);
                return null;
            }finally{
                if(urlConnection != null)
                    urlConnection.disconnect();
                if(reader != null){
                    try{
                        reader.close();
                    }catch (final IOException e){
                        Log.e("MainActivityFragment", "Error Closing Stream",e);
                    }
                }
            }
            return null;
        }

        /*protected void onPostExecute(){

        }*/
    }
}

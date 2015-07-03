package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /* (id == R.id.action_settings) {
            return true;
        }*/

        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_preferredLocation) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            String location = sharedPrefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

            Uri uriLocation = Uri.parse("geo:0,0?").buildUpon()
                                .appendQueryParameter("q",location).build();

           // String myUriLocation = uriLocation.toString();

            Intent mapLocation = new Intent(Intent.ACTION_VIEW, uriLocation);
            if (mapLocation.resolveActivity(getPackageManager()) != null) {
                startActivity(mapLocation);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}

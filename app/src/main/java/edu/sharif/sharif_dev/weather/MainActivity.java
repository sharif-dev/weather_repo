package edu.sharif.sharif_dev.weather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.gson.Gson;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout linearLayout = findViewById(R.id.linearLayout_search);
        Button search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*String searchText;
                EditText search_input = findViewById(R.id.search_inp);
                if (search_input.getText() != null & !Objects.requireNonNull(search_input.getText()).toString().equals("")) {
                    searchText = search_input.getText().toString(); // get query from user

                    ProgressBar progressBar = findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);
// make map box thread
                    GetMap getMap = new GetMap(searchText, getApplicationContext(), progressBar,linearLayout);
                    getMap.start();


                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.search_input_error, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    toast.show();
                }*/
                goToWeatherPage(45.6892,  40.3890, "", true);
            }
        });

    }

    /**
     * @param latitude:           if not connected, not important
     * @param longitude:          if not connected, not important
     * @param internetConnection: if not connected: false, else true
     * @param cityName:           name of the city searched
     */
    private void goToWeatherPage(double latitude, double longitude, String cityName, boolean internetConnection) {
        Intent intent = new Intent(this, WeatherForecastActivity.class);
        intent.putExtra(getString(R.string.latitude), latitude);
        intent.putExtra(getString(R.string.longitude), longitude);
        intent.putExtra(getString(R.string.cityName), cityName);
        // client has internet
        intent.putExtra(getString(R.string.internet_status), internetConnection);
        startActivity(intent);
    }
}

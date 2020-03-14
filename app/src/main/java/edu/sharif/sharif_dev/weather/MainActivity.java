package edu.sharif.sharif_dev.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                    GetMap getMap = new GetMap(searchText, getApplicationContext(), progressBar); // make map box thread
                    getMap.start();



                    // show forecast page
                    //gotoWeatherPage(42.3601, -71.0589);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.search_input_error, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    toast.show();
                }*/
                goToWeatherPage(42.3601, -71.0589);
            }
        });
    }

    private void goToWeatherPage(double latitude, double longitude){
        setContentView(R.layout.weather_forecast);
        ImageView waiting = findViewById(R.id.wait);
        TextView status = findViewById(R.id.result);
        WeatherForecast weatherForecast = new WeatherForecast(getApplicationContext(), getString(R.string.forecast_provider),
                getString(R.string.secret_key), waiting, status);
        weatherForecast.getWeather(latitude, longitude);
    }
}

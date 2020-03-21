package edu.sharif.sharif_dev.weather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String>  cityNames = new ArrayList<>();
        ListView list = findViewById(R.id.listview_search);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityNames);
        list.setAdapter(adapter);


        Button search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText;
                EditText search_input = findViewById(R.id.search_inp);
                if (search_input.getText() != null & !Objects.requireNonNull(search_input.getText()).toString().equals("")) {
                    searchText = search_input.getText().toString(); // get query from user

                    ProgressBar progressBar = findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.VISIBLE);
// make map box thread
                    GetMap getMap = new GetMap(searchText, getApplicationContext(), progressBar,cityNames,adapter);
                    getMap.start();


                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.search_input_error, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
      
      // goToWeatherPage(8 , 8);
    }

    private void goToWeatherPage(double latitude, double longitude){
        Intent intent = new Intent(this, WeatherForecastActivity.class);
        intent.putExtra(getString(R.string.latitude), latitude);
        intent.putExtra(getString(R.string.longitude), longitude);
        startActivity(intent);
    }
}


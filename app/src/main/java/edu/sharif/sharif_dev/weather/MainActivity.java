package edu.sharif.sharif_dev.weather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ArrayList<String>  cityNames = new ArrayList<>();
        final ListView list = findViewById(R.id.listview_search);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cityNames);
        list.setAdapter(adapter);
        final ArrayList<ArrayList<Double>> centerClasses = new ArrayList<>();

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
                    GetMap getMap = new GetMap(searchText, getApplicationContext(), progressBar,cityNames,adapter,centerClasses);
                    getMap.start();


                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.search_input_error, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d("pos",""+position);
                double latitude = centerClasses.get(position).get(0);
                double longitude = centerClasses.get(position).get(1);
                goToWeatherPage(latitude, longitude,true);

            }
        });
      // goToWeatherPage(8 , 8);

    }

    /**
     * @param latitude:           if not connected, not important
     * @param longitude:          if not connected, not important
     * @param internetConnection: if not connected: false, else true
     */
    private void goToWeatherPage(double latitude, double longitude, boolean internetConnection) {
        Intent intent = new Intent(this, WeatherForecastActivity.class);
        intent.putExtra(getString(R.string.latitude), latitude);
        intent.putExtra(getString(R.string.longitude), longitude);
        // client has internet
        intent.putExtra(getString(R.string.internet_status), internetConnection);
        startActivity(intent);
    }
}

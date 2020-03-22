package edu.sharif.sharif_dev.weather;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// check internet connection
        if(!checkConnection()){
            goToWeatherPage(0,0,"",false);
        }
        setContentView(R.layout.activity_main);


        final ArrayList<String> cityNames = new ArrayList<>();
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
                    GetMap.Builder builder = new GetMap.Builder();
                    builder = builder.withQuery(searchText);
                    builder = builder.withProgressBar(progressBar);
                    builder = builder.withContext(getApplicationContext());
                    builder = builder.withCityNames(cityNames);
                    builder = builder.withArrayAdapter(adapter);
                    builder = builder.withCenterClasses(centerClasses);

                    GetMap getMap = builder.build();
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
                double latitude = centerClasses.get(position).get(0);
                double longitude = centerClasses.get(position).get(1);

                System.out.println("lat: " + latitude + " long: " + longitude);

                goToWeatherPage(latitude, longitude, cityNames.get(position), true);

            }
        });
        //  goToWeatherPage(45.6892,  40.3890, "", true);

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
// to finish this activity  = not show main activity and use cache
        if(!internetConnection){
            finish();
        }
    }

    private boolean checkConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}

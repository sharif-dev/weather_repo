package edu.sharif.sharif_dev.weather.secondPage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import edu.sharif.sharif_dev.weather.R;

/**
 * activity for getting weather forecast from DarkSky.net
 */
public class WeatherForecastActivity extends AppCompatActivity {
    private Context context;
    private ImageView waitingGif;
    private ForecastResponse forecastResponse;
    private WeatherForecastDbHelper dbHelper;
    private Handler handler;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_forecast);
        waitingGif = findViewById(R.id.waiting_gif);
        findViewById(R.id.textView).setVisibility(View.GONE);
        context = getApplicationContext();
        Intent intent = getIntent();
        dbHelper = new WeatherForecastDbHelper(getApplicationContext());
        handler = new Handler();
        boolean internetStatus = intent.getBooleanExtra(getString(R.string.internet_status), false);

        // check internet access
        if (internetStatus) {
            double longitude = intent.getDoubleExtra(getString(R.string.longitude), 0);
            double latitude = intent.getDoubleExtra(getString(R.string.latitude), 0);
            cityName = intent.getStringExtra(getString(R.string.cityName));
            getWeather(latitude, longitude);
        } else {
            stopWaitingGif();
            findViewById(R.id.textView).setVisibility(View.VISIBLE);
            // no internet access, load from database
            noInternetViews();
        }
    }


    public void getWeather(final double latitude, final double longitude) {
        // Thread for sending request
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                String url = getString(R.string.forecast_provider) + getString(R.string.secret_key)
                        + "/" + latitude + "," + longitude;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                getResponse(response, true);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                });
                requestQueue.add(stringRequest);
            }
        }).start();
    }

    private void getResponse(final String response, boolean connected) {
        if (connected) {
            stopWaitingGif();
            saveResult(response);
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                forecastResponse = new Gson().fromJson(response, ForecastResponse.class);
                List<DailyData> dailyData = forecastResponse.daily.data;
                List<HourlyData> hourlyDataList = forecastResponse.hourly.data;

                // set ViewPager
                final ViewPager pager = findViewById(R.id.pager);
                final List<ScreenSlidePageFragment> fragments = new ArrayList<>();
                for (DailyData dailyDatum : dailyData) {
                    fragments.add(ScreenSlidePageFragment.getInstance(dailyDatum));
                }
                fragments.get(0).setHourlyData(hourlyDataList);
                pager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments));
                pager.setPageTransformer(true, new ParallaxPageTransformer());
                pager.setVisibility(View.VISIBLE);

                // show user guide
                Toast.makeText(context, getString(R.string.swipe_help),
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void handleError(VolleyError error) {
        stopWaitingGif();

        if (error instanceof NoConnectionError) {
            ConnectivityManager cm =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = null;
            if (cm != null) {
                activeNetwork = cm.getActiveNetworkInfo();
            }
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                Toast.makeText(context, getString(R.string.server_error),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, getString(R.string.not_connected),
                        Toast.LENGTH_SHORT).show();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ((ViewPager) findViewById(R.id.pager)).setVisibility(View.GONE);
                        findViewById(R.id.textView).setVisibility(View.VISIBLE);
                    }
                });
                // load from database
                noInternetViews();
                return;
            }
        } else if (error.getCause() == null) {
            ForecastResponseError responseError = new Gson().fromJson(new String(error.networkResponse.data)
                    , ForecastResponseError.class);
            Toast.makeText(context, getString(R.string.darksky_error) + responseError.getMessage(),
                    Toast.LENGTH_SHORT).show();
        } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException
                || (error.getCause().getMessage() != null
                && error.getCause().getMessage().contains(getString(R.string.connection)))) {
            Toast.makeText(context, getString(R.string.not_connected),
                    Toast.LENGTH_SHORT).show();
        } else if (error.getCause() instanceof MalformedURLException) {
            Toast.makeText(context, getString(R.string.bad_Request), Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                || error.getCause() instanceof JSONException
                || error.getCause() instanceof XmlPullParserException) {
            Toast.makeText(context, getString(R.string.parse_error),
                    Toast.LENGTH_SHORT).show();
        } else if (error.getCause() instanceof OutOfMemoryError) {
            Toast.makeText(context, getString(R.string.out_of_memory), Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(context, getString(R.string.request_error),
                    Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
            Toast.makeText(context, getString(R.string.server_not_responding), Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                || error.getCause() instanceof ConnectTimeoutException
                || error.getCause() instanceof SocketException
                || (error.getCause().getMessage() != null
                && error.getCause().getMessage().contains(getString(R.string.time_out)))) {
            Toast.makeText(context, getString(R.string.time_out),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, getString(R.string.unknown_error),
                    Toast.LENGTH_SHORT).show();
        }

        // return to main activity
        finish();
    }

    private void saveResult(final String response) {
        // Thread for writing to DataBase
        new Thread(new Runnable() {
            @Override
            public void run() {
                // get writable from database
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                // insert values into database
                Timestamp date = new Timestamp(new Date().getTime());
                values.put(WeatherForecastContract.FeedEntry.COLUMN_NAME_DATE, date.getTime());
                values.put(WeatherForecastContract.FeedEntry.COLUMN_NAME_JSON, response);
                values.put(WeatherForecastContract.FeedEntry.CITY_NAME, cityName);
                db.insert(WeatherForecastContract.FeedEntry.TABLE_NAME, null, values);
            }
        }).start();
    }

    private TreeMap<String, String[]> readFromLocalHistory() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                WeatherForecastContract.FeedEntry.COLUMN_NAME_DATE,
                WeatherForecastContract.FeedEntry.COLUMN_NAME_JSON,
                WeatherForecastContract.FeedEntry.CITY_NAME
        };
        // get all rows from database
        Cursor cursor = db.query(WeatherForecastContract.FeedEntry.TABLE_NAME,
                projection, null, null, null, null,
                "datetime(" + WeatherForecastContract.FeedEntry.COLUMN_NAME_DATE + ") DESC");
        TreeMap<String, String[]> result = new TreeMap<>();
        while (cursor.moveToNext()) {
            String date = cursor.getString(
                    cursor.getColumnIndexOrThrow(WeatherForecastContract.FeedEntry.COLUMN_NAME_DATE));
            String json = cursor.getString(
                    cursor.getColumnIndexOrThrow(WeatherForecastContract.FeedEntry.COLUMN_NAME_JSON));
            String city = cursor.getString(
                    cursor.getColumnIndexOrThrow(WeatherForecastContract.FeedEntry.CITY_NAME)
            );
            result.put(date, new String[]{json, city});
        }
        cursor.close();
        return result;
    }

    private void stopWaitingGif() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                waitingGif.setVisibility(View.GONE);
            }
        });
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private List<ScreenSlidePageFragment> fragments;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<ScreenSlidePageFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return 7;
        }

    }

    private void noInternetViews() {
        // run a thread for reading database and update UI
        new Thread(new Runnable() {
            @Override
            public void run() {
                final TreeMap<String, String[]> history = readFromLocalHistory();
                final ArrayList<String> stringList = new ArrayList<>();
                final ArrayList<String> keys = new ArrayList<>();

                for (String string : history.keySet()) {
                    String[] strings = history.get(string);
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM HH:mm");
                    stringList.add(strings[1] + "  " + sdf.format(new Date(Long.valueOf(string))));
                    keys.add(string);
                }
                Collections.reverse(stringList);
                Collections.reverse(keys);
                final ListView listView = findViewById(R.id.listView);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String response = history.get(keys.get(i))[0];
                        getResponse(response, false);
                    }
                });
                final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, stringList);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(arrayAdapter);
                    }
                });
            }
        }).start();
    }

}

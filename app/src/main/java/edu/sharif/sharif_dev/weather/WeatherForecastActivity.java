package edu.sharif.sharif_dev.weather;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * activity for getting weather forecast from DarkSky.net
 */
public class WeatherForecastActivity extends AppCompatActivity {
    private Context context;
    private ImageView waitingGif;
    private ForecastResponse forecastResponse;
    private WeatherForecastDbHelper dbHelper;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_forecast);
        context = getApplicationContext();
        waitingGif = findViewById(R.id.waiting_gif);
        Intent intent = getIntent();
        dbHelper = new WeatherForecastDbHelper(getApplicationContext());
        handler = new Handler();
        boolean internetStatus = intent.getBooleanExtra(getString(R.string.internet_status), false);
        if (internetStatus) {
            double longitude = intent.getDoubleExtra(getString(R.string.longitude), 0);
            double latitude = intent.getDoubleExtra(getString(R.string.latitude), 0);
            getWeather(latitude, longitude);
        } else {
            // todo no internet access
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
                                getResponse(response);
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

    private void getResponse(String response) {
        stopWaitingGif();

        saveResult(response);

        // todo
        System.out.println("response: " + response);
        // show result (without UI)

        this.forecastResponse = new Gson().fromJson(response, ForecastResponse.class);
        List<DailyData> dailyData = forecastResponse.daily.data;
        final ViewPager pager = findViewById(R.id.pager);
        final List<ScreenSlidePageFragment> fragments = new ArrayList<>();
        for (DailyData dailyDatum : dailyData) {
            fragments.add(ScreenSlidePageFragment.getInstance(dailyDatum));
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                pager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager(), fragments));
                pager.setPageTransformer(true, new ParallaxPageTransformer());
                pager.setVisibility(View.VISIBLE);

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
                Toast.makeText(context, "Server is not connected to internet.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Your device is not connected to internet.",
                        Toast.LENGTH_SHORT).show();
                // todo load from local (return)
            }
        } else if (error.getCause() == null) {
            ForecastResponseError responseError = new Gson().fromJson(new String(error.networkResponse.data)
                    , ForecastResponseError.class);
            Toast.makeText(context, "Error From DarkSky.net: " + responseError.getMessage(),
                    Toast.LENGTH_SHORT).show();
        } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException
                || (error.getCause().getMessage() != null
                && error.getCause().getMessage().contains("connection"))) {
            Toast.makeText(context, "Your device is not connected to internet.",
                    Toast.LENGTH_SHORT).show();
        } else if (error.getCause() instanceof MalformedURLException) {
            Toast.makeText(context, "Bad Request.", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException
                || error.getCause() instanceof JSONException
                || error.getCause() instanceof XmlPullParserException) {
            Toast.makeText(context, "Parse Error (because of invalid json or xml).",
                    Toast.LENGTH_SHORT).show();
        } else if (error.getCause() instanceof OutOfMemoryError) {
            Toast.makeText(context, "Out Of Memory Error.", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(context, "server couldn't find the authenticated request.",
                    Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError || error.getCause() instanceof ServerError) {
            Toast.makeText(context, "Server is not responding.", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException
                || error.getCause() instanceof ConnectTimeoutException
                || error.getCause() instanceof SocketException
                || (error.getCause().getMessage() != null
                && error.getCause().getMessage().contains("Connection timed out"))) {
            Toast.makeText(context, "Connection timeout error",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "An unknown error occurred.",
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
                Date date = Calendar.getInstance().getTime();
                values.put(WeatherForecastContract.FeedEntry.COLUMN_NAME_DATE, date.toString());
                values.put(WeatherForecastContract.FeedEntry.COLUMN_NAME_JSON, response);
                db.insert(WeatherForecastContract.FeedEntry.TABLE_NAME, null, values);
            }
        }).start();
    }

    private HashMap<String, String> readFromLocalHistory() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                WeatherForecastContract.FeedEntry.COLUMN_NAME_DATE,
                WeatherForecastContract.FeedEntry.COLUMN_NAME_JSON
        };
        // get all rows from database
        Cursor cursor = db.query(WeatherForecastContract.FeedEntry.TABLE_NAME,
                projection, null, null, null, null, null);
        HashMap<String, String> result = new HashMap<>();
        while (cursor.moveToNext()) {
            String date = cursor.getString(
                    cursor.getColumnIndexOrThrow(WeatherForecastContract.FeedEntry.COLUMN_NAME_DATE));
            String json = cursor.getString(
                    cursor.getColumnIndexOrThrow(WeatherForecastContract.FeedEntry.COLUMN_NAME_JSON));
            result.put(date, json);
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

}

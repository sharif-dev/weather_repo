package edu.sharif.sharif_dev.weather;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

/**
 * activity for getting weather forecast from DarkSky.net
 */
public class WeatherForecastActivity extends AppCompatActivity {
    private Context context;
    private ImageView waiting;
    private TextView status;
    private ForecastResponse forecastResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_forecast);
        context = getApplicationContext();
        waiting = findViewById(R.id.wait);
        status = findViewById(R.id.result);
        Intent intent = getIntent();
        double longitude = intent.getDoubleExtra(getString(R.string.longitude),0);
        double latitude = intent.getDoubleExtra(getString(R.string.latitude), 0);
        getWeather(latitude, longitude);
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
                                // todo do something with response
                                //Log.d("darksky", response);


                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
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
        waiting.setVisibility(View.GONE);


        System.out.println("response: " + response);
        // show result (without UI)
        status.setText(response);
        this.forecastResponse = new Gson().fromJson(response, ForecastResponse.class);
    }

    private void handleError(VolleyError error) {
        waiting.setVisibility(View.GONE);
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

        // todo return to first page.
        finish();
    }

}

package edu.sharif.sharif_dev.weather;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * class for getting forecast from DarkSky.net and processing it.
 */

public class WeatherForecast {
    private String forecastProvider;
    private String secretKey;
    private Context context;

    public WeatherForecast(Context context, String forecastProvider, String secretKey) {
        this.context = context;
        this.secretKey = secretKey;
        this.forecastProvider = forecastProvider;
    }

    public void getWeather(final double latitude,final double longitude){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                String url = forecastProvider + secretKey + "/"
                        + latitude + "," + longitude;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // do something with response
                                Log.d("darksky", response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // handle errors
                        Log.d("error_darksky", error.getMessage());
                    }
                });
                requestQueue.add(stringRequest);
            }
        }).start();
    }

}

package edu.sharif.sharif_dev.weather;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private ImageView wating;
    private TextView status;

    public WeatherForecast(Context context, String forecastProvider, String secretKey, ImageView wating
            , TextView status) {
        this.context = context;
        this.secretKey = secretKey;
        this.forecastProvider = forecastProvider;
        this.wating = wating;
        this.status = status;
    }

    public void getWeather(final double latitude, final double longitude) {

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
                                // todo do something with response
                                Log.d("darksky", response);

                                // remove waiting gif
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                wating.setVisibility(View.GONE);

                                // show result (without UI)
                                status.setText(response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // todo handle errors
                        Log.d("error_darksky", error.getMessage());
                    }
                });
                requestQueue.add(stringRequest);
            }
        }).start();
    }

}

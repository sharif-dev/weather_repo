package edu.sharif.sharif_dev.weather;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class GetMap extends Thread {
    private String query;
    private Context context;
    private ProgressBar progressBar;
    private final String accessToken = "pk.eyJ1IjoiemFocmF5b3VzZWZpIiwiYSI6ImNrN3A3OTB4NjA3OTQzZnJybm44Nmh4YW0ifQ.WDrn4Q_HTxBV8D53wtemYA";

    GetMap(String query, Context context, ProgressBar progressBar){
        this.query = query;
        this.context = context;
        this.progressBar = progressBar;
    }

    public void run(){

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://api.mapbox.com/geocoding/v5/mapbox.places/{"+query+"}.json?access_token={"+accessToken+"}";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("tag","Response is: "+ response.substring(0,500));
                        progressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag","Error");
                progressBar.setVisibility(View.GONE);
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

}

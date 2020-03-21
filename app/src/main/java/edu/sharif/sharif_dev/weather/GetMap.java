package edu.sharif.sharif_dev.weather;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;


public class GetMap extends Thread {
    private String query;
    private Context context;
    private ProgressBar progressBar;
    private final ArrayList<String> cityNames;
    private ArrayAdapter<String> arrayAdapter;
    private final String accessToken = "pk.eyJ1IjoiemFocmF5b3VzZWZpIiwiYSI6ImNrN3A3OTB4NjA3OTQzZnJybm44Nmh4YW0ifQ.WDrn4Q_HTxBV8D53wtemYA";

    GetMap(String query, Context context, ProgressBar progressBar, ArrayList<String> cityNames, ArrayAdapter<String> arrayAdapter){
        this.query = query;
        this.context = context;
        this.progressBar = progressBar;
        this.cityNames = cityNames;
        this.arrayAdapter = arrayAdapter;
    }

    @Override
    public void run(){

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://api.mapbox.com/geocoding/v5/mapbox.places/"+query+".json?access_token="+accessToken;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("tag","Get Response");
                        try {
                            Gson gson = new Gson();

                            MapClass mapClass = gson.fromJson(response, MapClass.class);

                            for( int i = 0 ;i< mapClass.getFeatures().size();i++){
                                cityNames.add(mapClass.getFeatures().get(i).getPlace_name());
                            }

                            sendToUI();

                        }catch (Exception e){
                            showError();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag",error.getMessage());
                progressBar.setVisibility(View.GONE);
                showError();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
    void showError(){
        Toast toast = Toast.makeText(context, R.string.mapbox_error, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
        toast.show();
    }
    private void sendToUI(){
        Runnable showCode = new Runnable() {
            @Override
            public void run() {
                arrayAdapter.notifyDataSetChanged();
            }
        };
        Handler handler = new Handler();
        handler.post(showCode);
    }

}
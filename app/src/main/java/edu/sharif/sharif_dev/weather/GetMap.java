package edu.sharif.sharif_dev.weather;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

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
    private CustomHandler ch;
    private ProgressBar progressBar;
    private final ArrayList<String> cityNames;
    private ArrayList<ArrayList<Double>> centerClasses;
    private ArrayAdapter<String> arrayAdapter;
    private final String accessToken = "pk.eyJ1IjoiemFocmF5b3VzZWZpIiwiYSI6ImNrN3A3OTB4NjA3OTQzZnJybm44Nmh4YW0ifQ.WDrn4Q_HTxBV8D53wtemYA";

    static class Builder {
        String query;
        Context context;
        ProgressBar progressBar;
        ArrayList<String> cityNames;
        ArrayList<ArrayList<Double>> centerClasses = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter;

        Builder withQuery(String query) {
            this.query = query;
            return this;
        }

        Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        Builder withProgressBar(ProgressBar progressBar) {
            this.progressBar = progressBar;
            return this;
        }

        Builder withCityNames(ArrayList<String> cityNames) {
            this.cityNames = cityNames;
            return this;
        }

        Builder withArrayAdapter(ArrayAdapter<String> adapter) {
            this.arrayAdapter = adapter;
            return this;
        }

        Builder withCenterClasses(ArrayList<ArrayList<Double>> centerClasses) {
            this.centerClasses = centerClasses;
            return this;
        }

        GetMap build() {
            return new GetMap(query, context, progressBar, cityNames, arrayAdapter, centerClasses);
        }

    }

    private GetMap(String query, Context context, ProgressBar progressBar, ArrayList<String> cityNames, ArrayAdapter<String> arrayAdapter, ArrayList<ArrayList<Double>> centerClasses) {
        this.query = query;
        this.context = context;
        this.progressBar = progressBar;
        this.cityNames = cityNames;
        this.arrayAdapter = arrayAdapter;
        this.centerClasses = centerClasses;
        ch = new CustomHandler(context);
    }

    @Override
    public void run() {
// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://api.mapbox.com/geocoding/v5/mapbox.places/" + query + ".json?access_token=" + accessToken;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        Log.d("tag", "Get Response");
                        try {
                            Gson gson = new Gson();

                            MapClass mapClass = gson.fromJson(response, MapClass.class);
                            centerClasses.clear();
                            cityNames.clear(); // clear the last list

                            for (int i = 0; i < mapClass.getFeatures().size(); i++) {
                                cityNames.add(mapClass.getFeatures().get(i).getPlace_name());
                                centerClasses.add(mapClass.getFeatures().get(i).getCenter());
                            }

                            sendToUI();

                        } catch (Exception e) {
                            showError(R.string.mapbox_error);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (!handleError(error.networkResponse.statusCode)) {
                    // if not handled
                    showError(R.string.mapbox_error);
                }
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private boolean handleError(int code) {
        switch (code) {
            case 401:
                showError(R.string.token_error);
                return true;
            case 403:
                showError(R.string.forbidden);
                return true;
            case 404:
                showError(R.string.not_found);
                return true;
            case 422:
                showError(R.string.query_length_error);
                return true;
            case 429:
                showError(R.string.rate_error);
                return true;
            default:
                return false;
        }
    }

    private void showError(int stringId) {
        // send to handler
        ch.sendIntMessage(stringId);
    }

    //TODO
    private void sendToUI() {
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
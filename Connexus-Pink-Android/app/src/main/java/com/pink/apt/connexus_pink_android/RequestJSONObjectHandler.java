package com.pink.apt.connexus_pink_android;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

public class RequestJSONObjectHandler {

    String url;
    RequestQueue queue;
    String jsonResponse;
    JSONObject response;
    String TAG = "JSONObjectHandler";

    private RequestJSONObjectHandler() {};

    public RequestJSONObjectHandler(String url, RequestQueue queue) {
        this.url = url;
        this.queue = queue;
    }

    public void getJSONObject() {
        JsonArrayRequest req = new JsonArrayRequest(this.url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        Log.d(TAG, Integer.toString(response.length()));

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject stream = (JSONObject) response.get(i);

                                String name = stream.getString("name");
                                String coverImageURL = stream.getString("coverImageURL");
                                String id = stream.getString("id");

                                jsonResponse += "Name: " + name + "\n\n";
                                jsonResponse += "coverImageURL: " + coverImageURL + "\n\n";
                                jsonResponse += "id: " + id + "\n\n";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG,jsonResponse);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });
        Log.d("getJSONObject","request sent");
        this.queue.add(req);
    }


}

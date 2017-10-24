package com.pink.apt.connexus_pink_android.backend;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pink.apt.connexus_pink_android.RecyclerAdapter;
import com.pink.apt.connexus_pink_android.models.StreamModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by matt on 10/22/17.
 */

public abstract class RequestJSONArrayHandler {
    String url;
    RequestQueue queue;
    String jsonResponse;
    JSONObject response;
    String TAG = "JSONObjectHandler";

    protected RequestJSONArrayHandler() {};

    public void getJSONObject(final RecyclerAdapter adapter) {
        JsonArrayRequest req = new JsonArrayRequest(this.url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        Log.d(TAG, Integer.toString(response.length()));

                        clearAdapter(adapter);
                        //make an array of createlists
                        //pass the array of createlists to adapter
                        //notify adapter to update

                        try {
                            // Parsing json array response
                            // loop through each json object
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject stream = (JSONObject) response.get(i);

                                StreamModel streamModel = processJSONObject(adapter, stream);
                                addAdapter(adapter, streamModel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });
        this.queue.add(req);
    }

    public abstract StreamModel processJSONObject(RecyclerAdapter adapter, JSONObject stream);

    public abstract void clearAdapter(RecyclerAdapter adapter);

    public abstract void addAdapter(RecyclerAdapter adapter, StreamModel streamModel);

}

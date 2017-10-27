package com.pink.apt.connexus_pink_android.backend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pink.apt.connexus_pink_android.RecyclerAdapter;
import com.pink.apt.connexus_pink_android.models.StreamModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.pink.apt.connexus_pink_android.GlobalVars.EMPTY_COVER_IMAGE_URL;

/**
 * Created by matt on 10/22/17.
 */

public class NearbyJSONArrayHandler {

    final public static String ACTION_NEARBY_SEARCH_COMPLETE = "com.pink.apt.connexus_pink_android.ACTION_NEARBY_SEARCH_COMPLETE";
    final public static String EXTRA_NEXT_CURSOR = "com.pink.apt.connexus_pink_android.EXTRA_NEXT_CURSOR";
    final public static String EXTRA_NEXT_BOOL = "com.pink.apt.connexus_pink_android.EXTRA_NEXT_BOOL";
    final public static String EXTRA_DISTANCE = "com.pink.apt.connexus_pink_android.EXTRA_DISTANCE";
    final public static String EXTRA_STREAM_ID = "com.pink.apt.connexus_pink_android.EXTRA_STREAM_ID";
    final public static String EXTRA_IMAGE_URL = "com.pink.apt.connexus_pink_android.EXTRA_IMAGE_URL";

    private String url;
    private RequestQueue queue;
    String TAG = "JSONObjectHandler";
    private Context context;

    private NearbyJSONArrayHandler() {};

    public NearbyJSONArrayHandler(String url, RequestQueue queue, Context context){
        this.url = url;
        this.queue = queue;
        this.context = context;
    }

    public void getJSONObject() {
        JsonArrayRequest req = new JsonArrayRequest(this.url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        Log.d(TAG, Integer.toString(response.length()));

                        //make an array of createlists
                        //pass the array of createlists to adapter
                        //notify adapter to update
                        String nextPage = "";
                        String nextBool = "false";
                        ArrayList<String> streamId = new ArrayList<>();
                        ArrayList<String> imageUrl = new ArrayList<>();
                        ArrayList<String> distance = new ArrayList<>();

                        try {
                            // Parsing json array response
                            // loop through each json object
                            JSONObject stream = new JSONObject();

                            // expect to go through this loop only once. this will just protect it from null returns
                            for (int i = 0; i < response.length(); i++){
                                stream = (JSONObject) response.get(i);
                                nextBool = stream.getString("next_page"); // this looks wrong but what is coming from the page is named inconsistently
                                nextPage = stream.getString("next_cursor"); // this looks wrong but what is coming from the page is named inconsistently
                            }

                            JSONArray foundStreams = stream.getJSONArray("found_streams");
                            for (int i = 0; i < foundStreams.length(); i++) {
                                streamId.add(((JSONObject) foundStreams.get(i)).getString("stream_id"));
                                imageUrl.add(((JSONObject) foundStreams.get(i)).getString("image_url"));
                                distance.add(((JSONObject) foundStreams.get(i)).getString("distance"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent newIntent = new Intent(ACTION_NEARBY_SEARCH_COMPLETE);
                        newIntent.putExtra(EXTRA_DISTANCE, distance);
                        newIntent.putExtra(EXTRA_STREAM_ID, streamId);
                        newIntent.putExtra(EXTRA_IMAGE_URL, imageUrl);
                        newIntent.putExtra(EXTRA_NEXT_CURSOR, nextPage);
                        newIntent.putExtra(EXTRA_NEXT_BOOL, nextBool);
                        context.sendBroadcast(newIntent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        Log.d(TAG, "Requested to this url " + this.url);
        this.queue.add(req);
    }



}

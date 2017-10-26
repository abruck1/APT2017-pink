package com.pink.apt.connexus_pink_android.backend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
    private ArrayList<NearbyPackage> nearbyPackageArrayList;

    private NearbyJSONArrayHandler() {};

    public NearbyJSONArrayHandler(String url, RequestQueue queue, Context context){
        this.url = url;
        this.queue = queue;
        this.context = context;
        this.nearbyPackageArrayList = new ArrayList<>();
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
                        nearbyPackageArrayList.clear();

                        try {
                            // Parsing json array response
                            // loop through each json object
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject stream = (JSONObject) response.get(i);
                                nearbyPackageArrayList.add(processJSONObject(stream));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });

        // send broadcast with results
        // unpackage results into String arrays

        String streamId[] = new String[this.nearbyPackageArrayList.size()];
        String imageUrl[] = new String[this.nearbyPackageArrayList.size()];
        String nextCursorUrl[] = new String[this.nearbyPackageArrayList.size()];
        String nextBool[] = new String[this.nearbyPackageArrayList.size()];
        String distance[] = new String[this.nearbyPackageArrayList.size()];

        for(int i=0; i<this.nearbyPackageArrayList.size(); i++){
            NearbyPackage currentPackage = new NearbyPackage();
            streamId[i] = currentPackage.streamId;
            imageUrl[i] = currentPackage.imageUrl;
            nextCursorUrl[i] = currentPackage.nextCursorUrl;
            nextBool[i] = currentPackage.nextBool;
            distance[i] = currentPackage.distance;
        }

        Intent newIntent = new Intent(ACTION_NEARBY_SEARCH_COMPLETE);
        newIntent.putExtra(EXTRA_DISTANCE, distance);
        newIntent.putExtra(EXTRA_STREAM_ID, streamId);
        newIntent.putExtra(EXTRA_IMAGE_URL, imageUrl);
        newIntent.putExtra(EXTRA_NEXT_CURSOR, nextCursorUrl);
        newIntent.putExtra(EXTRA_NEXT_BOOL, nextBool);
        context.sendBroadcast(newIntent);

        Log.d(TAG, "Requested to this url " + this.url);
        this.queue.add(req);
    }

    private NearbyPackage processJSONObject(JSONObject stream) {

        NearbyPackage nearbyPackage = new NearbyPackage();
        try {
            nearbyPackage.streamId = stream.getString("stream_id");
            nearbyPackage.imageUrl = stream.getString("image_url");
            nearbyPackage.nextCursorUrl = stream.getString("next_cursor");
            nearbyPackage.nextBool = stream.getString("next");
            nearbyPackage.distance = stream.getString("distance");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return nearbyPackage;
    }

    private class NearbyPackage{
        String streamId;
        String imageUrl;
        String nextCursorUrl;
        String nextBool;
        String distance;
    }

}

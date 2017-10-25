package com.pink.apt.connexus_pink_android.backend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.pink.apt.connexus_pink_android.RecyclerAdapter;
import com.pink.apt.connexus_pink_android.ViewAllRecyclerAdapter;
import com.pink.apt.connexus_pink_android.models.StreamModel;
import com.pink.apt.connexus_pink_android.models.ViewAllStreamData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.pink.apt.connexus_pink_android.GlobalVars.EMPTY_COVER_IMAGE_URL;

/**
 * Created by matt on 10/22/17.
 */

public class SearchJSONHandler extends RequestJSONArrayHandler {

    public static final String NUMFOUND = "com.pink.apt.connexus_pink_android.EXTRA_NUMFOUND";
    public static final String SEARCH_COMPLETE = "com.pink.apt.connexus_pink_android.ACTION_SEARCH_COMPLETE";

    private Context context;

    private SearchJSONHandler(){};

    public SearchJSONHandler(String url, RequestQueue queue, Context context) {
        this.url = url;
        this.queue = queue;
        this.context = context;
    }

    @Override
    public StreamModel processJSONObject(RecyclerAdapter adapter, JSONObject stream) {
        ViewAllStreamData streamModel = new ViewAllStreamData();
        String TAG2 = "SEARCHING";
        try {
            String numFound = stream.getString("num_results");

            Intent newIntent = new Intent(SEARCH_COMPLETE);
            newIntent.putExtra(NUMFOUND, numFound);
            context.sendBroadcast(newIntent);
            Log.d(TAG2, "numFound=" + numFound);


            JSONArray foundStreams = stream.getJSONArray("found_streams");
            for(int i = 0; i < foundStreams.length(); i++) {
                String streamId = ((JSONObject) foundStreams.get(i)).getString("stream_id");
                String coverImageURL = ((JSONObject) foundStreams.get(i)).getString("stream_coverImageURL");
                String name = ((JSONObject) foundStreams.get(i)).getString("stream_name");
                Log.d(TAG2, "Stream_name=" + name + " StreamId=" + streamId);

                streamModel.setId(streamId);
                streamModel.setStreamName(name);
                if (coverImageURL.isEmpty()) {
                    Log.d(TAG, "Name" + name + " was empty");
                    streamModel.setStreamUrl(EMPTY_COVER_IMAGE_URL);
                } else {
                    streamModel.setStreamUrl(coverImageURL);
                }
                if(i != foundStreams.length()-1) {
                    addAdapter(adapter, streamModel);
                    streamModel = new ViewAllStreamData();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return streamModel;
    }

    public void clearAdapter(RecyclerAdapter adapter){
        ViewAllRecyclerAdapter newAdapter = (ViewAllRecyclerAdapter) adapter;
        newAdapter.galleryList.clear();
    }

    public void addAdapter(RecyclerAdapter adapter, StreamModel streamModel){
        ViewAllRecyclerAdapter newAdapter = (ViewAllRecyclerAdapter) adapter;
        newAdapter.galleryList.add((ViewAllStreamData) streamModel);
    }
}

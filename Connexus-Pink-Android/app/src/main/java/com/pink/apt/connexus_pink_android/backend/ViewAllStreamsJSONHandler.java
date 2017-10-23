package com.pink.apt.connexus_pink_android.backend;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.pink.apt.connexus_pink_android.RecyclerAdapter;
import com.pink.apt.connexus_pink_android.ViewAllRecyclerAdapter;
import com.pink.apt.connexus_pink_android.models.StreamModel;
import com.pink.apt.connexus_pink_android.models.ViewAllStreamData;

import org.json.JSONException;
import org.json.JSONObject;

import static com.pink.apt.connexus_pink_android.GlobalVars.EMPTY_COVER_IMAGE_URL;

/**
 * Created by matt on 10/22/17.
 */

public class ViewAllStreamsJSONHandler extends RequestJSONArrayHandler {

    private ViewAllStreamsJSONHandler(){};

    public ViewAllStreamsJSONHandler(String url, RequestQueue queue){
        this.url = url;
        this.queue = queue;
    }

    @Override
    public StreamModel processJSONObject(RecyclerAdapter adapter, JSONObject stream) {

        ViewAllStreamData streamModel = new ViewAllStreamData();

        try {
            String name = stream.getString("name");
            String coverImageURL = stream.getString("coverImageURL");
            String id = stream.getString("id");

            streamModel.setId(id);
            streamModel.setStreamName(name);
            if (coverImageURL.isEmpty()) {
                Log.d(TAG, "Name" + name + " was empty");
                streamModel.setStreamUrl(EMPTY_COVER_IMAGE_URL);
            } else {
                streamModel.setStreamUrl(coverImageURL);
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

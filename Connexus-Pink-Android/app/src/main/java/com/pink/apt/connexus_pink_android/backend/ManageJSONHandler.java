package com.pink.apt.connexus_pink_android.backend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.pink.apt.connexus_pink_android.ManageRecyclerAdapter;
import com.pink.apt.connexus_pink_android.RecyclerAdapter;
import com.pink.apt.connexus_pink_android.models.ManageStreamData;
import com.pink.apt.connexus_pink_android.models.StreamModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.pink.apt.connexus_pink_android.GlobalVars.EMPTY_COVER_IMAGE_URL;
import static com.pink.apt.connexus_pink_android.GlobalVars.MANAGE_URL;

/**
 * Created by matt on 10/22/17.
 */

public class ManageJSONHandler extends RequestJSONArrayHandler {

    private Context context;

    private ManageJSONHandler(){};

    public ManageJSONHandler(String url, RequestQueue queue, Context context) {
        this.url = url;
        this.queue = queue;
        this.context = context;
    }

    @Override
    public StreamModel processJSONObject(RecyclerAdapter adapter, JSONObject stream) {
        ManageStreamData streamModel = new ManageStreamData();
        String TAG2 = "MANAGE";
        try {
            JSONArray userStreams;
            String ownedStreams = MANAGE_URL+"#1";
            if (this.url.equals(ownedStreams)) {
                userStreams = stream.getJSONArray("user_streams");
                Log.d(TAG2, "Doing user_streams");
            } else {
                userStreams = stream.getJSONArray("subscribe");
                Log.d(TAG2, "Doing subscribe");
            }
            for(int i = 0; i < userStreams.length(); i++) {
                String streamId = ((JSONObject) userStreams.get(i)).getString("stream_id");
                String coverImageURL = ((JSONObject) userStreams.get(i)).getString("stream_coverImageURL");
                String name = ((JSONObject) userStreams.get(i)).getString("stream_name");
                Log.d(TAG2, "Stream_name=" + name + " StreamId=" + streamId);

                String numViews = ((JSONObject) userStreams.get(i)).getString("stream_views");
                String lastPicDate = ((JSONObject) userStreams.get(i)).getString("stream_last_pic_date");
                String numImages = ((JSONObject) userStreams.get(i)).getString("stream_number_of_images");
                Log.d(TAG2, "Stream_name=" + name + " StreamNumImages=" + numImages);


                streamModel.setId(streamId);
                streamModel.setStreamName(name);
                streamModel.setViewCount("Number of Views: " + numViews);
                streamModel.setLastPicDate("Date last image was uploaded: " + lastPicDate);
                streamModel.setNumImages("Number of Images: " + numImages);
                if (coverImageURL.isEmpty()) {
                    Log.d(TAG, "Name" + name + " was empty");
                    streamModel.setStreamUrl(EMPTY_COVER_IMAGE_URL);
                } else {
                    streamModel.setStreamUrl(coverImageURL);
                }
                if(i != userStreams.length()-1) {
                    addAdapter(adapter, streamModel);
                    streamModel = new ManageStreamData();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return streamModel;
    }

    public void clearAdapter(RecyclerAdapter adapter){
        ManageRecyclerAdapter newAdapter = (ManageRecyclerAdapter) adapter;
        newAdapter.galleryList.clear();
    }

    public void addAdapter(RecyclerAdapter adapter, StreamModel streamModel){
        ManageRecyclerAdapter newAdapter = (ManageRecyclerAdapter) adapter;
        newAdapter.galleryList.add((ManageStreamData) streamModel);
    }
}

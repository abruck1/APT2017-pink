package com.pink.apt.connexus_pink_android.backend;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.pink.apt.connexus_pink_android.RecyclerAdapter;
import com.pink.apt.connexus_pink_android.ViewRecyclerAdapter;
import com.pink.apt.connexus_pink_android.models.StreamModel;
import com.pink.apt.connexus_pink_android.models.ViewStreamData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.pink.apt.connexus_pink_android.GlobalVars.EMPTY_COVER_IMAGE_URL;

/**
 * Created by matt on 10/22/17.
 */

public class ViewStreamJSONHandler extends RequestJSONArrayHandler {

    private ViewStreamJSONHandler(){};

    public ViewStreamJSONHandler(String url, RequestQueue queue){
        this.url = url;
        this.queue = queue;
    }

    @Override
    public StreamModel processJSONObject(RecyclerAdapter adapter, JSONObject stream) {

        ViewRecyclerAdapter myAdapter = (ViewRecyclerAdapter) adapter;
        ViewStreamData streamModel;
        if (myAdapter.galleryList.size() == 0) {
            streamModel = new ViewStreamData();
        } else {
            streamModel = myAdapter.galleryList.get(0);
        }

        try {
            String name = stream.getString("stream_name");
            JSONArray coverImageURL = stream.getJSONArray("image_urls");
            String nextCursorUrl = stream.getString("next_cursor");
            ArrayList<String> arr = new ArrayList<>();
            for(int i = 0; i < coverImageURL.length(); i++) {
                if(coverImageURL.getString(i).isEmpty()) {
                    arr.add(EMPTY_COVER_IMAGE_URL);
                } else {
                    arr.add(coverImageURL.getString(i));
                }
            }

            String id = stream.getString("stream_id");
            Log.d(TAG, arr.toString());
            streamModel.setId(id);
            streamModel.setStreamName(name);
            streamModel.appendImageUrls(arr);
            streamModel.setNextCursorUrl(nextCursorUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return streamModel;
    }

    public void clearAdapter(RecyclerAdapter adapter){
        ViewRecyclerAdapter newAdapter = (ViewRecyclerAdapter) adapter;
        newAdapter.galleryList.clear();
    }

    public void addAdapter(RecyclerAdapter adapter, StreamModel streamModel){
        ViewRecyclerAdapter newAdapter = (ViewRecyclerAdapter) adapter;
        newAdapter.galleryList.add((ViewStreamData) streamModel);
    }
}

package com.pink.apt.connexus_pink_android.backend;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.pink.apt.connexus_pink_android.RecyclerAdapter;
import com.pink.apt.connexus_pink_android.ViewAllRecyclerAdapter;
import com.pink.apt.connexus_pink_android.ViewRecyclerAdapter;
import com.pink.apt.connexus_pink_android.models.StreamModel;
import com.pink.apt.connexus_pink_android.models.ViewAllStreamData;
import com.pink.apt.connexus_pink_android.models.ViewStreamData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

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

        ViewStreamData streamModel = new ViewStreamData();

        try {
            String name = stream.getString("stream_name");
            JSONArray coverImageURL = stream.getJSONArray("image_urls");
            String[] arr = new String[coverImageURL.length()];
            for(int i = 0; i < coverImageURL.length(); i++) {
                arr[i] = coverImageURL.getString(i);
                if(arr[i].isEmpty()) arr[i] = EMPTY_COVER_IMAGE_URL;
            }

            String id = stream.getString("stream_id");

            streamModel.setId(id);
            streamModel.setStreamName(name);
            streamModel.setImageUrls(arr);
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

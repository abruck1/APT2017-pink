package com.pink.apt.connexus_pink_android.backend;

import android.content.Context;
import android.content.Intent;
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

    final public static String ACTION_FETCH_STREAM_COMPLETE = "com.pink.apt.connexus_pink_android.ACTION_FETCH_STREAM_COMPLETE";
    final public static String EXTRA_PREV_CURSOR = "com.pink.apt.connexus_pink_android.EXTRA_PREV_CURSOR";
    final public static String EXTRA_NEXT_CURSOR = "com.pink.apt.connexus_pink_android.EXTRA_NEXT_CURSOR";
    final public static String EXTRA_PREV_BOOL = "com.pink.apt.connexus_pink_android.EXTRA_PREV_BOOL";
    final public static String EXTRA_NEXT_BOOL = "com.pink.apt.connexus_pink_android.EXTRA_NEXT_BOOL";
    private ViewStreamJSONHandler(){};
    private Context context;

    public ViewStreamJSONHandler(String url, RequestQueue queue, Context context){
        this.url = url;
        this.queue = queue;
        this.context = context;
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
            String prevCursorUrl = stream.getString("prev_cursor");
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
            String prevBool = stream.getString("prev");
            String nextBool = stream.getString("next");

            Intent newIntent = new Intent(ACTION_FETCH_STREAM_COMPLETE);
            newIntent.putExtra(EXTRA_PREV_CURSOR, prevCursorUrl);
            newIntent.putExtra(EXTRA_NEXT_CURSOR, nextCursorUrl);
            newIntent.putExtra(EXTRA_PREV_BOOL, prevBool);
            newIntent.putExtra(EXTRA_NEXT_BOOL, nextBool);
            context.sendBroadcast(newIntent);

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

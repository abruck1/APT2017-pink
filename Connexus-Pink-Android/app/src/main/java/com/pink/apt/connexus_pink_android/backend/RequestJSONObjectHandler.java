package com.pink.apt.connexus_pink_android.backend;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.pink.apt.connexus_pink_android.CreateList;
import com.pink.apt.connexus_pink_android.RecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.pink.apt.connexus_pink_android.GlobalVars.EMPTY_COVER_IMAGE_URL;

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

    public void getJSONObject(final RecyclerAdapter adapter) {
        JsonArrayRequest req = new JsonArrayRequest(this.url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        Log.d(TAG, Integer.toString(response.length()));

                        adapter.galleryList.clear();
                        //make an array of createlists
                        //pass the array of createlists to adapter
                        //notify adapter to update

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject stream = (JSONObject) response.get(i);

                                String name = stream.getString("name");
                                String coverImageURL = stream.getString("coverImageURL");
                                String id = stream.getString("id");

                                CreateList newList = new CreateList();

                                newList.setId(id);
                                newList.setStreamName(name);
                                if(coverImageURL.isEmpty()){
                                    Log.d(TAG, "Name" + name +  " was empty");
                                    newList.setStreamUrl(EMPTY_COVER_IMAGE_URL);
                                } else {
                                    newList.setStreamUrl(coverImageURL);
                                }

                                adapter.galleryList.add(newList);

                                jsonResponse += "Name: " + name + "\n\n";
                                jsonResponse += "coverImageURL: " + coverImageURL + "\n\n";
                                jsonResponse += "id: " + id + "\n\n";

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
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

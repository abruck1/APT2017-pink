package com.pink.apt.connexus_pink_android;

import com.android.volley.RequestQueue;
import com.pink.apt.connexus_pink_android.backend.RequestJSONObjectHandler;

/**
 * Created by matt on 10/18/17.
 */

public class MultiStreamParser {

    String url;
    RequestQueue queue;

    private MultiStreamParser() {};

    public MultiStreamParser(String url, RequestQueue queue){
        this.url = url;
        this.queue = queue;
        new RequestJSONObjectHandler(url, queue).getJSONObject();
    }

}

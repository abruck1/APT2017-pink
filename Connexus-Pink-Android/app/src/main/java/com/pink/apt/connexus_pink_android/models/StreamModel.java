package com.pink.apt.connexus_pink_android.models;

/**
 * Created by matt on 10/18/17.
 */

public abstract class StreamModel {
    protected String streamName;
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String android_version_name) {
        this.streamName = android_version_name;
    }


}

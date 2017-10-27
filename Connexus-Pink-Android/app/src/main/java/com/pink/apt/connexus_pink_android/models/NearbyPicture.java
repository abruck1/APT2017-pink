package com.pink.apt.connexus_pink_android.models;

/**
 * Created by weyma on 10/25/2017.
 */

public class NearbyPicture {
    private String streamId;
    private String url;
    private String latitude;
    private String longitude;
    private String distanceFromDevice;
    private String streamName;

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDistanceFromDevice() {
        return distanceFromDevice;
    }

    public void setDistanceFromDevice(String distanceFromDevice) {
        this.distanceFromDevice = distanceFromDevice;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }
}

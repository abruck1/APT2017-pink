package com.pink.apt.connexus_pink_android.models;

/**
 * Created by matt on 10/22/17.
 */

public class ManageStreamData extends StreamModel {

    private String streamUrl;
    private String numImages;
    private String lastPicDate;
    private String viewCount;

    public String getViewCount() {
        return viewCount;
    }
    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getLastPicDate() {
        return lastPicDate;
    }
    public void setLastPicDate(String lastPicDate) {
        this.lastPicDate = lastPicDate;
    }

    public String getNumImages() {
        return numImages;
    }
    public void setNumImages(String numImages) {
        this.numImages = numImages;
    }

    public String getStreamUrl() {
        return streamUrl;
    }
    public void setStreamUrl(String android_image_url) {
        this.streamUrl = android_image_url;
    }
}

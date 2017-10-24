package com.pink.apt.connexus_pink_android.models;

import java.util.ArrayList;

/**
 * Created by matt on 10/22/17.
 */

public class ViewStreamData extends StreamModel {

    private ArrayList<String> imageUrls = new ArrayList<>();

    private String nextCursorUrl;

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void appendImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls.addAll(imageUrls);
    }

    public String getNextCursorUrl() {
        return nextCursorUrl;
    }

    public void setNextCursorUrl(String nextCursorUrl) {
        this.nextCursorUrl = nextCursorUrl;
    }

}

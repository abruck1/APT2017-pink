package com.pink.apt.connexus_pink_android.models;

import java.util.ArrayList;

/**
 * Created by matt on 10/22/17.
 */

public class ViewStreamData extends StreamModel {

    private String[] imageUrls;

    private String nextCursorUrl;

    public String[] getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getNextCursorUrl() {
        return nextCursorUrl;
    }

    public void setNextCursorUrl(String nextCursorUrl) {
        this.nextCursorUrl = nextCursorUrl;
    }

}

package com.pink.apt.connexus_pink_android.models;

import java.util.ArrayList;

/**
 * Created by weyma on 10/25/2017.
 */

public class NearbyPictures {
    private boolean prevCursor;
    private boolean nextCursor;
    private String prevUrl;
    private String nextUrl;
    private ArrayList<NearbyPicture> nearbyPictures;

    public boolean hasPrevCursor() {
        return prevCursor;
    }

    public void setPrevCursor(boolean prevCursor) {
        this.prevCursor = prevCursor;
    }

    public boolean hasNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(boolean nextCursor) {
        this.nextCursor = nextCursor;
    }

    public String getPrevUrl() {
        return prevUrl;
    }

    public void setPrevUrl(String prevUrl) {
        this.prevUrl = prevUrl;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public void updateNearbyPictures(ArrayList<NearbyPicture> newPictures){
        this.nearbyPictures.addAll(newPictures);
    }

    public ArrayList<NearbyPicture> getNearbyPictures(){
        return this.nearbyPictures;
    }

    public NearbyPictures(){
        this.nearbyPictures = new ArrayList<>();
    }

}

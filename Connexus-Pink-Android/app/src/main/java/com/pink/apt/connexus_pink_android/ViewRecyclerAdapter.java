package com.pink.apt.connexus_pink_android;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pink.apt.connexus_pink_android.activities.ViewStreamActivity;
import com.pink.apt.connexus_pink_android.models.StreamModel;
import com.pink.apt.connexus_pink_android.models.ViewStreamData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by matt on 10/22/17.
 */

public class ViewRecyclerAdapter extends RecyclerAdapter {

    private static final String TAG = "ViewRecyclerAdapter";
    public ArrayList<ViewStreamData> galleryList;

    public ViewRecyclerAdapter(Context context, ArrayList<ViewStreamData> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        if (this.galleryList.size() == 0) {
            return 0;
        } else {
            Log.d(TAG, Integer.toString(galleryList.get(0).getImageUrls().size()));
            return this.galleryList.get(0).getImageUrls().size();
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        Picasso.with(context).load(galleryList.get(0).getImageUrls().get(i)).resize(75, 75).into(viewHolder.img);
    }
}

package com.pink.apt.connexus_pink_android;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.pink.apt.connexus_pink_android.activities.ViewStreamActivity;
import com.pink.apt.connexus_pink_android.models.StreamModel;
import com.pink.apt.connexus_pink_android.models.ViewStreamData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by matt on 10/22/17.
 */

public class ViewRecyclerAdapter extends RecyclerAdapter {

    public ArrayList<ViewStreamData> galleryList;

    public ViewRecyclerAdapter(Context context, ArrayList<ViewStreamData> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return galleryList.get(0).getImageUrls().length;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        Picasso.with(context).load(galleryList.get(0).getImageUrls()[i]).resize(75, 75).into(viewHolder.img);
    }
}

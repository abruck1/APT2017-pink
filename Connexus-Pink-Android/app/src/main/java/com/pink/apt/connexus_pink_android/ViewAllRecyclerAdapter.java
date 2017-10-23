package com.pink.apt.connexus_pink_android;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.pink.apt.connexus_pink_android.activities.ViewStreamActivity;
import com.pink.apt.connexus_pink_android.models.StreamModel;
import com.pink.apt.connexus_pink_android.models.ViewAllStreamData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by matt on 10/22/17.
 */

public class ViewAllRecyclerAdapter extends RecyclerAdapter {

    public ArrayList<ViewAllStreamData> galleryList;

    public ViewAllRecyclerAdapter(Context context, ArrayList<ViewAllStreamData> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.title.setText(galleryList.get(i).getStreamName());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(context).load(galleryList.get(i).getStreamUrl()).resize(75, 75).into(viewHolder.img);
        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent = new Intent(context, ViewStreamActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, galleryList.get(viewHolder.getAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });
    }
}
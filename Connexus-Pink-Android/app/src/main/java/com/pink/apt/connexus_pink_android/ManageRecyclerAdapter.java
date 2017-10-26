package com.pink.apt.connexus_pink_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pink.apt.connexus_pink_android.activities.ViewStreamActivity;
import com.pink.apt.connexus_pink_android.models.ManageStreamData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ManageRecyclerAdapter extends RecyclerAdapter {
    protected Context context;
    public ArrayList<ManageStreamData> galleryList;

    public ManageRecyclerAdapter(Context context, ArrayList<ManageStreamData> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
    }

    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, int position) {
        holder.title.setText(galleryList.get(position).getStreamName());
        holder.imgCount.setText(galleryList.get(holder.getAdapterPosition()).getNumImages());
        holder.viewCount.setText(galleryList.get(holder.getAdapterPosition()).getViewCount());
        holder.lastPicDate.setText(galleryList.get(holder.getAdapterPosition()).getLastPicDate());
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(context).load(galleryList.get(position).getStreamUrl()).resize(75, 75).into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent = new Intent(context, ViewStreamActivity.class);
                Bundle extras = new Bundle();
                extras.putString("STREAM_ID", galleryList.get(holder.getAdapterPosition()).getId());
                extras.putString("STREAM_NAME", galleryList.get(holder.getAdapterPosition()).getStreamName());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });
        holder.img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (holder.img.getAlpha() == 0.5f) {
                    holder.img.setAlpha(1f); // unselected
                } else {
                    holder.img.setAlpha(0.5f); // selected
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }
}
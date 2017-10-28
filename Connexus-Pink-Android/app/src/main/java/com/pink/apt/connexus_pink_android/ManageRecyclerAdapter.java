package com.pink.apt.connexus_pink_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.pink.apt.connexus_pink_android.activities.ViewStreamActivity;
import com.pink.apt.connexus_pink_android.models.ManageStreamData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageRecyclerAdapter extends RecyclerAdapter {
    protected Context context;
    private String TAG = "ManageRecyclerAdapter";
    public ArrayList<ManageStreamData> galleryList;
    public ArrayList<Integer> selectedList;

    public ManageRecyclerAdapter(Context context, ArrayList<ManageStreamData> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
        this.selectedList = new ArrayList<>();
    }

    public void onBindViewHolder(final RecyclerAdapter.ViewHolder holder, final int position) {
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
                    selectedList.remove(selectedList.indexOf(position));
                } else {
                    holder.img.setAlpha(0.5f); // selected
                    selectedList.add(position);
                }
                Log.d(TAG, "Selected List=" + selectedList.toString());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public void removeFromGellryList (int index) {
        this.galleryList.remove(index);
    }
}
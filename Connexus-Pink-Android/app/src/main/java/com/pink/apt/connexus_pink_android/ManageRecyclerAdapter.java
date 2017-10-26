package com.pink.apt.connexus_pink_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pink.apt.connexus_pink_android.activities.ViewStreamActivity;
import com.pink.apt.connexus_pink_android.models.ManageStreamData;
import com.pink.apt.connexus_pink_android.models.ViewAllStreamData;
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
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView title;
        protected ImageView img;
        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            img = view.findViewById(R.id.img);
        }
    }
}
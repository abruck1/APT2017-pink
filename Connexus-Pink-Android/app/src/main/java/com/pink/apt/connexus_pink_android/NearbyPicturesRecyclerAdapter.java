package com.pink.apt.connexus_pink_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pink.apt.connexus_pink_android.activities.ViewStreamActivity;
import com.pink.apt.connexus_pink_android.models.NearbyPictures;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by matt on 10/18/17.
 */

public class NearbyPicturesRecyclerAdapter extends RecyclerView.Adapter<NearbyPicturesRecyclerAdapter.ViewHolder> {
    protected Context context;
    private NearbyPictures nearbyPictures;

    public NearbyPicturesRecyclerAdapter(Context context, NearbyPictures nearbyPictures) {
        this.nearbyPictures = nearbyPictures;
        this.context = context;
    }

    public void updateNearbyPictures(NearbyPictures nearbyPictures){
        this.nearbyPictures = nearbyPictures;
    }

    @Override
    public NearbyPicturesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Picasso.with(context).load(nearbyPictures.getNearbyPictures().get(position).getUrl()).resize(75, 75).into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final Intent intent = new Intent(context, ViewStreamActivity.class);
                Bundle extras = new Bundle();
                extras.putString("STREAM_ID", nearbyPictures.getNearbyPictures().get(holder.getAdapterPosition()).getStreamId());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });
        holder.title.setText(nearbyPictures.getNearbyPictures().get(position).getDistanceFromDevice());
    }

    @Override
    public int getItemCount() {
        return this.nearbyPictures.getNearbyPictures().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView title;
        protected ImageView img;
        public ViewHolder(View view) {
            super(view);

            title = (TextView)view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
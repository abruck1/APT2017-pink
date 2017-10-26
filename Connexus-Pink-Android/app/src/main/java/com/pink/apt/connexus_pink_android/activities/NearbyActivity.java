package com.pink.apt.connexus_pink_android.activities;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pink.apt.connexus_pink_android.NearbyPicturesRecyclerAdapter;
import com.pink.apt.connexus_pink_android.R;
import com.pink.apt.connexus_pink_android.ViewAllRecyclerAdapter;
import com.pink.apt.connexus_pink_android.models.NearbyPicture;
import com.pink.apt.connexus_pink_android.models.NearbyPictures;

import java.util.ArrayList;

public class NearbyActivity extends AppCompatActivity {

    RequestQueue queue;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button viewAllStreamsButton;
    Button moreButton;
    NearbyPictures nearbyPictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        // setup json volly queue
        queue = Volley.newRequestQueue(this);
        queue.start();

        // setup nearby pictures to manage what goes into the adapter
        nearbyPictures = new NearbyPictures();

        // define recyclerview and initialize
        recyclerView = (RecyclerView) findViewById(R.id.imagegallery_nearby);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setVisibility(View.INVISIBLE);

        // start progress bar
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_view_nearby);
        progressBar.setVisibility(View.VISIBLE);

        // setup nestedscroll view and define size
        final NestedScrollView nsv = (NestedScrollView) findViewById(R.id.nested_scroll_view_nearby);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int a = (displaymetrics.heightPixels * 60) / 100;
        nsv.getLayoutParams().height = a;

        // setup layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView.setLayoutManager(layoutManager);

        // setup recyclerview adapter for nearby
        NearbyPicturesRecyclerAdapter adapter = new NearbyPicturesRecyclerAdapter(this, nearbyPictures);
        recyclerView.setAdapter(adapter);


    }
}

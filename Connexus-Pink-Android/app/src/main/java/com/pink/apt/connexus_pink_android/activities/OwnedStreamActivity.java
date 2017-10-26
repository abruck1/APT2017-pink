package com.pink.apt.connexus_pink_android.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pink.apt.connexus_pink_android.ManageRecyclerAdapter;
import com.pink.apt.connexus_pink_android.R;
import com.pink.apt.connexus_pink_android.backend.ManageJSONHandler;
import com.pink.apt.connexus_pink_android.models.ManageStreamData;
import java.util.ArrayList;

import static com.pink.apt.connexus_pink_android.GlobalVars.MANAGE_URL;

/**
 * Created by ari on 10/26/17.
 */

public class OwnedStreamActivity extends AppCompatActivity {
    RequestQueue queue;
    String TAG = "OwnedStreamActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owned_streams);

        queue = Volley.newRequestQueue(this);
        queue.start();

        RecyclerView recyclerView = findViewById(R.id.owned_imagegallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setVisibility(View.GONE);

        ProgressBar progressBar = findViewById(R.id.progress_bar_owned);
        progressBar.setVisibility(View.VISIBLE);

        final NestedScrollView nsv = findViewById(R.id.nested_scroll_owned_streams);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int a = (displaymetrics.heightPixels * 40) / 100;
        nsv.getLayoutParams().height = a;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<ManageStreamData> streamsList = new ArrayList<>();

        ManageRecyclerAdapter adapter = new ManageRecyclerAdapter(this, streamsList);
        recyclerView.setAdapter(adapter);

        ManageJSONHandler returnedJson = new ManageJSONHandler(MANAGE_URL+"#1", queue, this);
        returnedJson.getJSONObject(adapter, progressBar, recyclerView);



        Button deleteStream = findViewById(R.id.manage_delete_stream);
        deleteStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
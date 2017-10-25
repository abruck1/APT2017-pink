package com.pink.apt.connexus_pink_android.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pink.apt.connexus_pink_android.ViewAllRecyclerAdapter;
import com.pink.apt.connexus_pink_android.ViewRecyclerAdapter;
import com.pink.apt.connexus_pink_android.models.StreamModel;
import com.pink.apt.connexus_pink_android.RecyclerAdapter;
import com.pink.apt.connexus_pink_android.R;
import com.pink.apt.connexus_pink_android.backend.ViewAllStreamsJSONHandler;
import com.pink.apt.connexus_pink_android.models.ViewAllStreamData;

import java.util.ArrayList;

import static com.pink.apt.connexus_pink_android.GlobalVars.*;

public class ViewAllStreamsActivity extends AppCompatActivity {


    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_layout);
        queue = Volley.newRequestQueue(this);
        queue.start();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setVisibility(View.GONE);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_view_all_streams);
        progressBar.setVisibility(View.VISIBLE);

        final SearchView searchBox = findViewById(R.id.view_all_search_text_field);
        searchBox.setQueryHint("Search for Streams and Tags");

        searchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getBaseContext(), "Searching for: " + query, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        final NestedScrollView nsv = (NestedScrollView) findViewById(R.id.nested_scroll_view_all_streams);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int a = (displaymetrics.heightPixels * 40) / 100;
        nsv.getLayoutParams().height = a;

        int b = (displaymetrics.widthPixels * 45) / 100;
        searchBox.getLayoutParams().width = b;

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<ViewAllStreamData> streamsList = new ArrayList<>();

        ViewAllRecyclerAdapter adapter = new ViewAllRecyclerAdapter(this, streamsList);
        recyclerView.setAdapter(adapter);
        ViewAllStreamsJSONHandler returnedJson = new ViewAllStreamsJSONHandler(VIEW_ALL_STREAMS_URL, queue);
        returnedJson.getJSONObject(adapter, progressBar, recyclerView);

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.INTERNET)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET
                    );

            // MY_PERMISSIONS_REQUEST_INTERNET is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}


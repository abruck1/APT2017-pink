package com.pink.apt.connexus_pink_android.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pink.apt.connexus_pink_android.NearbyPicturesRecyclerAdapter;
import com.pink.apt.connexus_pink_android.R;
import com.pink.apt.connexus_pink_android.backend.NearbyJSONArrayHandler;
import com.pink.apt.connexus_pink_android.backend.ViewStreamJSONHandler;
import com.pink.apt.connexus_pink_android.models.NearbyPictures;

import static com.pink.apt.connexus_pink_android.GlobalVars.NEARBY_URL;

public class NearbyActivity extends AppCompatActivity {

    RequestQueue queue;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button viewAllStreamsButton;
    Button moreButton;
    NearbyPictures nearbyPictures;
    String url;
    String currentLong;
    String currentLat;
    NearbyStreamReceiver nearbyStreamReceiver;

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
        int a = (displaymetrics.heightPixels * 45) / 100;
        nsv.getLayoutParams().height = a;

        // setup layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        recyclerView.setLayoutManager(layoutManager);

        // setup recyclerview adapter for nearby
        NearbyPicturesRecyclerAdapter adapter = new NearbyPicturesRecyclerAdapter(this, nearbyPictures);
        recyclerView.setAdapter(adapter);

        // get users current location
        // check for user permissions
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        // create first url to send request to
        url = NEARBY_URL + "long=" + currentLong + "&lat=" + currentLat;
        // send JSON request
        NearbyJSONArrayHandler n

        // setup buttons
        viewAllStreamsButton = findViewById(R.id.nearby_view_all_streams_button);
        viewAllStreamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        moreButton = findViewById(R.id.nearby_more_button);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private final LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            currentLong = Double.toString(location.getLongitude());
            currentLat = Double.toString(location.getLatitude());
        }

        @Override
        public void onStatusChanged(String s, int integer, Bundle bundle){
            return;
        }

        @Override
        public void onProviderEnabled(String s){
            return;
        }

        @Override
        public void onProviderDisabled(String s) {
            return;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter fetchStreamCompleteFilter = new IntentFilter(ViewStreamJSONHandler.ACTION_FETCH_STREAM_COMPLETE);
        fetchStreamCompleteFilter.addCategory(Intent.CATEGORY_DEFAULT);
        nearbyStreamReceiver = new NearbyActivity.NearbyStreamReceiver();
        registerReceiver(nearbyStreamReceiver, fetchStreamCompleteFilter);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(nearbyStreamReceiver);
    }

    public class NearbyStreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            switch (action) {
                case(ViewStreamJSONHandler.ACTION_FETCH_STREAM_COMPLETE) :
                    Bundle extras = intent.getExtras();
                    prevCursor = extras.getString(ViewStreamJSONHandler.EXTRA_PREV_CURSOR);
                    nextCursor = extras.getString(ViewStreamJSONHandler.EXTRA_NEXT_CURSOR);
                    String prevBool = extras.getString(ViewStreamJSONHandler.EXTRA_PREV_BOOL);
                    String nextBool = extras.getString(ViewStreamJSONHandler.EXTRA_NEXT_BOOL);
                    if(prevBool.equals("false")){
                        Log.d(TAG, "prevCursor is empty");
                        prevButton.setVisibility(View.INVISIBLE);
                    } else{
                        Log.d(TAG, "prevCursor is not empty");
                        prevButton.setVisibility(View.VISIBLE);
                    }

                    if(nextBool.equals("false")){
                        Log.d(TAG, "nextCursor is empty");
                        nextButton.setVisibility(View.INVISIBLE);
                    } else{
                        Log.d(TAG, "nextCursor is not empty");
                        nextButton.setVisibility(View.VISIBLE);
                    }

                    return;
            }
        }
    }
}

package com.pink.apt.connexus_pink_android.activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pink.apt.connexus_pink_android.NearbyPicturesRecyclerAdapter;
import com.pink.apt.connexus_pink_android.R;
import com.pink.apt.connexus_pink_android.backend.NearbyJSONArrayHandler;
import com.pink.apt.connexus_pink_android.backend.ViewStreamJSONHandler;
import com.pink.apt.connexus_pink_android.models.NearbyPicture;
import com.pink.apt.connexus_pink_android.models.NearbyPictures;

import java.util.ArrayList;

import static com.pink.apt.connexus_pink_android.GlobalVars.NEARBY_URL;

public class NearbyActivity extends AppCompatActivity {

    RequestQueue queue;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Button viewAllStreamsButton;
    LinearLayout buttonContainer;
    Button moreButton;
    NearbyPictures nearbyPictures;
    String url;
    String currentLong = "0";
    String currentLat = "0";
    String nextPage = "1";
    LocationManager lm;
    NearbyStreamReceiver nearbyStreamReceiver;
    NearbyPicturesRecyclerAdapter adapter;
    public static int MY_PERMISSIONS_REQUEST_COURSE_LOCATION = 1;
    public static int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 2;
    Location location;
    String TAG = "NearbyActivity";

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
        adapter = new NearbyPicturesRecyclerAdapter(this, nearbyPictures);
        recyclerView.setAdapter(adapter);

        // get users current location
        // check for user permissions
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                MY_PERMISSIONS_REQUEST_COURSE_LOCATION
        );

        ActivityCompat.requestPermissions(
                this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_FINE_LOCATION
        );

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        if(hasCourseLocationPermission() && hasFineLocationPermission()) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                currentLong = Double.toString(location.getLongitude());
                currentLat = Double.toString(location.getLatitude());
                Log.d(TAG, "Lat,Long: " + currentLat + "," + currentLong);
            }
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        // create first url to send request to
        updateUrl();
        // send JSON request
        NearbyJSONArrayHandler jsonHandler = new NearbyJSONArrayHandler(url, queue, this);
        jsonHandler.getJSONObject();

        // setup buttons
        viewAllStreamsButton = findViewById(R.id.nearby_view_all_streams_button);
        viewAllStreamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonContainer = findViewById(R.id.nearby_more_button_container);
        buttonContainer.setVisibility(View.GONE);

        moreButton = findViewById(R.id.nearby_more_button);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NearbyJSONArrayHandler jsonHandler = new NearbyJSONArrayHandler(url, queue, getApplicationContext());
                jsonHandler.getJSONObject();
                progressBar.setVisibility(View.VISIBLE);
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
        IntentFilter nearbyStreamCompleteFilter = new IntentFilter(NearbyJSONArrayHandler.ACTION_NEARBY_SEARCH_COMPLETE);
        nearbyStreamCompleteFilter.addCategory(Intent.CATEGORY_DEFAULT);
        nearbyStreamReceiver = new NearbyActivity.NearbyStreamReceiver();
        registerReceiver(nearbyStreamReceiver, nearbyStreamCompleteFilter);

        if(hasCourseLocationPermission() && hasFineLocationPermission()) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(nearbyStreamReceiver);

//        lm.removeUpdates(locationListener);
    }

    public class NearbyStreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            switch (action) {
                case(NearbyJSONArrayHandler.ACTION_NEARBY_SEARCH_COMPLETE) :
                    Bundle extras = intent.getExtras();
                    nextPage = extras.getString(NearbyJSONArrayHandler.EXTRA_NEXT_CURSOR);
                    String nextBool = extras.getString(NearbyJSONArrayHandler.EXTRA_NEXT_BOOL);
                    ArrayList<String> urls = extras.getStringArrayList(NearbyJSONArrayHandler.EXTRA_IMAGE_URL);
                    ArrayList<String> distances = extras.getStringArrayList(NearbyJSONArrayHandler.EXTRA_DISTANCE);
                    ArrayList<String> ids = extras.getStringArrayList(NearbyJSONArrayHandler.EXTRA_STREAM_ID);
                    ArrayList<String> streamNames = extras.getStringArrayList(NearbyJSONArrayHandler.EXTRA_STREAM_NAME);

                    updateUrl();

                    for(int i=0; i<urls.size(); i++){
                        NearbyPicture nearbyPicture = new NearbyPicture();
                        nearbyPicture.setUrl(urls.get(i));
                        Double distance = Double.parseDouble(distances.get(i));
                        if(distance<1){
                            nearbyPicture.setDistanceFromDevice("<1 mi");
                        }else {
                            nearbyPicture.setDistanceFromDevice(Integer.toString(distance.intValue()) + " mi");
                        }
                        nearbyPicture.setStreamId(ids.get(i));
                        nearbyPicture.setStreamName(streamNames.get(i));
                        nearbyPictures.getNearbyPictures().add(nearbyPicture);
                    }

                    adapter.updateNearbyPictures(nearbyPictures);
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                    if(nextBool.equals("false")){
                        Log.d(TAG, "nextCursor is empty");
                        buttonContainer.setVisibility(View.GONE);
                    } else{
                        Log.d(TAG, "nextCursor is not empty");
                        buttonContainer.setVisibility(View.VISIBLE);
                    }

                    return;
            }
        }
    }

    private boolean hasCourseLocationPermission()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

            if (PermissionChecker.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    private boolean hasFineLocationPermission()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

            if (PermissionChecker.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void updateUrl(){
        url = NEARBY_URL + "long=" + currentLong + "&lat=" + currentLat + "&p=" + nextPage;
    }
}

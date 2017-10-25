package com.pink.apt.connexus_pink_android.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
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
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pink.apt.connexus_pink_android.R;
import com.pink.apt.connexus_pink_android.ViewRecyclerAdapter;
import com.pink.apt.connexus_pink_android.backend.ViewStreamJSONHandler;
import com.pink.apt.connexus_pink_android.models.ViewStreamData;

import java.util.ArrayList;

import static com.pink.apt.connexus_pink_android.GlobalVars.MY_PERMISSIONS_REQUEST_INTERNET;
import static com.pink.apt.connexus_pink_android.GlobalVars.VIEW_STREAM_URL;

public class ViewStreamActivity extends AppCompatActivity {
    String TAG = "ViewStreamActivity";
    String nextCursor;
    String prevCursor;
    String completePrevUrl;
    String completeNextUrl;
    String streamId;
    String streamName;
    Button prevButton;
    Button nextButton;
    RequestQueue queue;
    FetchStreamReceiver fetchStreamReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);

        // initialize volley queue
        queue = Volley.newRequestQueue(this);
        queue.start();

        // get stream id from intent
        Bundle extras = this.getIntent().getExtras();
        streamId = extras.getString("STREAM_ID");
        streamName = extras.getString("STREAM_NAME");

        TextView streamNameTitle = findViewById(R.id.view_stream);
        streamNameTitle.setText(streamName);
        
        // create and set progress bar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_view_stream);
        progressBar.setVisibility(View.VISIBLE);

        // setup recyclerview
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        // setup nestedscrollview
        final NestedScrollView nsv = (NestedScrollView) findViewById(R.id.nested_scroll_view_stream);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int a = (displaymetrics.heightPixels * 40) / 100;
        nsv.getLayoutParams().height = a;

        // setup recycler layout manager
        final GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<ViewStreamData> streamsImages = new ArrayList<>();
        Log.d(TAG, "length=" + streamsImages.size());

        // set recycler view adapter
        final ViewRecyclerAdapter adapter = new ViewRecyclerAdapter(this, streamsImages);
        recyclerView.setAdapter(adapter);

        // make JSON call to get stream images
        ViewStreamJSONHandler returnedJson = new ViewStreamJSONHandler(VIEW_STREAM_URL + streamId, queue, this);
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


        // setup button onclicklisteners
        Button uploadButton = (Button) findViewById(R.id.upload_image);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                //pass the StreamID, StreamName, and UploadURL
                Bundle b = new Bundle();
                b.putString("streamID", streamId);
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });

        Button streamsButton = (Button) findViewById(R.id.back_to_all_streams);
        streamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        prevButton = (Button) findViewById(R.id.prev_button);
        prevButton.setVisibility(View.INVISIBLE);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completePrevUrl = VIEW_STREAM_URL + streamId + "?prev_cursor=" + prevCursor;
                ViewStreamJSONHandler returnedJson = new ViewStreamJSONHandler(completePrevUrl, queue, getApplicationContext());
                returnedJson.getJSONObject(adapter, progressBar, recyclerView);
            }
        });

        nextButton = (Button) findViewById(R.id.next_button);
        nextButton.setVisibility(View.INVISIBLE);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeNextUrl = VIEW_STREAM_URL + streamId + "?next_cursor=" + nextCursor;
                ViewStreamJSONHandler returnedJson = new ViewStreamJSONHandler(completeNextUrl, queue, getApplicationContext());
                returnedJson.getJSONObject(adapter, progressBar, recyclerView);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter fetchStreamCompleteFilter = new IntentFilter(ViewStreamJSONHandler.ACTION_FETCH_STREAM_COMPLETE);
        fetchStreamCompleteFilter.addCategory(Intent.CATEGORY_DEFAULT);
        fetchStreamReceiver = new FetchStreamReceiver();
        registerReceiver(fetchStreamReceiver, fetchStreamCompleteFilter);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(fetchStreamReceiver);
    }

    public class FetchStreamReceiver extends BroadcastReceiver {
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

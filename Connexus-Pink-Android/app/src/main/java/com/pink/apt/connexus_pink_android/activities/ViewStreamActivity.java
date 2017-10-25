package com.pink.apt.connexus_pink_android.activities;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pink.apt.connexus_pink_android.EndlessRecyclerViewScrollListener;
import com.pink.apt.connexus_pink_android.R;
import com.pink.apt.connexus_pink_android.ViewRecyclerAdapter;
import com.pink.apt.connexus_pink_android.backend.ViewStreamJSONHandler;
import com.pink.apt.connexus_pink_android.models.ViewStreamData;

import java.util.ArrayList;

import static com.pink.apt.connexus_pink_android.GlobalVars.MY_PERMISSIONS_REQUEST_INTERNET;
import static com.pink.apt.connexus_pink_android.GlobalVars.VIEW_STREAM_URL;

public class ViewStreamActivity extends AppCompatActivity {
    String TAG = "ViewStreamActivity";
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);
        queue = Volley.newRequestQueue(this);
        queue.start();

        Bundle extras = this.getIntent().getExtras();
        final String streamId = extras.getString(Intent.EXTRA_TEXT);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_view_stream);
        progressBar.setVisibility(View.VISIBLE);

        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<ViewStreamData> streamsImages = new ArrayList<>();
        Log.d(TAG, "length=" + streamsImages.size());

        final ViewRecyclerAdapter adapter = new ViewRecyclerAdapter(this, streamsImages);
        recyclerView.setAdapter(adapter);
        ViewStreamJSONHandler returnedJson = new ViewStreamJSONHandler(VIEW_STREAM_URL + streamId, queue);
        returnedJson.getJSONObject(adapter, progressBar, recyclerView);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                String suffixUrl = adapter.galleryList.get(0).getNextCursorUrl();
                String finalUrl = VIEW_STREAM_URL + streamId + "?next_cursor=" + suffixUrl;
                ViewStreamJSONHandler returnedJSON = new ViewStreamJSONHandler(finalUrl, queue);
                returnedJSON.getJSONObject(adapter, progressBar, recyclerView);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

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
    }

}

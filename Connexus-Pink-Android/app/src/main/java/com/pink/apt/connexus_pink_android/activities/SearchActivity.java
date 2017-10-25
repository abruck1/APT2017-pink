package com.pink.apt.connexus_pink_android.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pink.apt.connexus_pink_android.R;
import com.pink.apt.connexus_pink_android.ViewAllRecyclerAdapter;
import com.pink.apt.connexus_pink_android.backend.SearchJSONHandler;
import com.pink.apt.connexus_pink_android.backend.ViewAllStreamsJSONHandler;
import com.pink.apt.connexus_pink_android.models.ViewAllStreamData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.pink.apt.connexus_pink_android.GlobalVars.SEARCH_URL;
import static com.pink.apt.connexus_pink_android.GlobalVars.VIEW_ALL_STREAMS_URL;

/**
 * Created by ari on 10/24/17.
 */



public class SearchActivity extends AppCompatActivity {

    RequestQueue queue;
    String TAG = "SearchActivity";
    TextView resultsText;
    String searchString;
    SearchReceiver searchReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Bundle extras = this.getIntent().getExtras();
        searchString = extras.getString(Intent.EXTRA_TEXT);
        Log.d(TAG, "Search string: " + searchString);

        resultsText = findViewById(R.id.results_text);
        final SearchView searchBox = findViewById(R.id.search_searchbox_field);
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

        queue = Volley.newRequestQueue(this);
        queue.start();

        RecyclerView recyclerView = findViewById(R.id.search_imagegallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setVisibility(View.GONE);

        ProgressBar progressBar = findViewById(R.id.progress_bar_search);
        progressBar.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<ViewAllStreamData> streamsList = new ArrayList<>();

        ViewAllRecyclerAdapter adapter = new ViewAllRecyclerAdapter(this, streamsList);
        recyclerView.setAdapter(adapter);

        String encodedUrl = "";
        try {
            encodedUrl = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String queryStringUrl = "search=" + encodedUrl + "&p=1";
        String finalUrl = SEARCH_URL + queryStringUrl;

        SearchJSONHandler returnedJson = new SearchJSONHandler(finalUrl, queue, this);
        returnedJson.getJSONObject(adapter, progressBar, recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter searchComplete = new IntentFilter(SearchJSONHandler.SEARCH_COMPLETE);
        searchComplete.addCategory(Intent.CATEGORY_DEFAULT);
        searchReceiver = new SearchReceiver();
        registerReceiver(searchReceiver, searchComplete);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(searchReceiver);
    }

    public class SearchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            Bundle extras = intent.getExtras();
            final String numFound = extras.getString(SearchJSONHandler.NUMFOUND);
            switch (action) {
                case(SearchJSONHandler.SEARCH_COMPLETE) :
                    String message = numFound + " results for: " + "<b>" + searchString + "</b> \nclick on an image to view the stream";
                    resultsText.setText(Html.fromHtml(message));
                    return;
            }
        }
    }
}

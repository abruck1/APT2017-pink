package com.pink.apt.connexus_pink_android.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pink.apt.connexus_pink_android.R;
import com.pink.apt.connexus_pink_android.ViewAllRecyclerAdapter;
import com.pink.apt.connexus_pink_android.backend.SearchJSONHandler;
import com.pink.apt.connexus_pink_android.models.ViewAllStreamData;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.pink.apt.connexus_pink_android.GlobalVars.SEARCH_URL;

/**
 * Created by ari on 10/24/17.
 */



public class SearchActivity extends AppCompatActivity {

    RequestQueue queue;
    String TAG = "SearchActivity";
    TextView resultsText;
    String searchString;
    SearchReceiver searchReceiver;
    Button prevButton;
    Button nextButton;
    String nextCursor;
    String prevCursor;
    String encodedUrl = "";

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

        final RecyclerView recyclerView = findViewById(R.id.search_imagegallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setVisibility(View.GONE);

        // setup nestedscrollview
        final NestedScrollView nsv = findViewById(R.id.nested_scroll_search_streams);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int a = (displaymetrics.heightPixels * 40) / 100;
        nsv.getLayoutParams().height = a;

        final ProgressBar progressBar = findViewById(R.id.progress_bar_search);
        progressBar.setVisibility(View.VISIBLE);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<ViewAllStreamData> streamsList = new ArrayList<>();

        final ViewAllRecyclerAdapter adapter = new ViewAllRecyclerAdapter(this, streamsList);
        recyclerView.setAdapter(adapter);

        try {
            encodedUrl = URLEncoder.encode(searchString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String queryStringUrl = "search=" + encodedUrl + "&p=1";
        String finalUrl = SEARCH_URL + queryStringUrl;

        SearchJSONHandler returnedJson = new SearchJSONHandler(finalUrl, queue, this);
        returnedJson.getJSONObject(adapter, progressBar, recyclerView);

        prevButton = findViewById(R.id.search_prev_button);
        prevButton.setVisibility(View.INVISIBLE);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String queryStringUrl = "search=" + encodedUrl + "&p=" + prevCursor;
                String finalUrl = SEARCH_URL + queryStringUrl;

                SearchJSONHandler returnedJson = new SearchJSONHandler(finalUrl, queue, getApplicationContext());
                returnedJson.getJSONObject(adapter, progressBar, recyclerView);
            }
        });

        nextButton = findViewById(R.id.search_next_button);
        nextButton.setVisibility(View.INVISIBLE);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String queryStringUrl = "search=" + encodedUrl + "&p=" + nextCursor;
                String finalUrl = SEARCH_URL + queryStringUrl;

                SearchJSONHandler returnedJson = new SearchJSONHandler(finalUrl, queue, getApplicationContext());
                returnedJson.getJSONObject(adapter, progressBar, recyclerView);
            }
        });
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
                    String searchString = extras.getString(SearchJSONHandler.SEARCHSTRING);
                    String message = numFound + " results for: " + "<b>" + searchString + "</b> \nclick on an image to view the stream";
                    Log.d(TAG, "message=" + message);
                    resultsText.setText(Html.fromHtml(message));
                    String nextBool = extras.getString(SearchJSONHandler.NEXTBOOL);
                    nextCursor = extras.getString(SearchJSONHandler.NEXTCURSOR);
                    String prevBool = extras.getString(SearchJSONHandler.PREVBOOL);
                    prevCursor = extras.getString(SearchJSONHandler.PREVCURSOR);

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
            }
        }
    }
}

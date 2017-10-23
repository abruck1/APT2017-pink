package com.pink.apt.connexus_pink_android.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.pink.apt.connexus_pink_android.RecyclerAdapter;
import com.pink.apt.connexus_pink_android.R;
import com.pink.apt.connexus_pink_android.ViewAllRecyclerAdapter;
import com.pink.apt.connexus_pink_android.ViewRecyclerAdapter;
import com.pink.apt.connexus_pink_android.models.StreamModel;
import com.pink.apt.connexus_pink_android.models.ViewAllStreamData;

import java.util.ArrayList;

public class ViewStreamActivity extends AppCompatActivity {
    String TAG = "ViewStreamActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);

        ViewAllStreamData streamModel = new ViewAllStreamData();
        ArrayList<StreamModel> streamList = new ArrayList<>();

        Bundle extras = this.getIntent().getExtras();
        String streamId = extras.getString(Intent.EXTRA_TEXT);
        Log.d(TAG, "**********Stream id" + streamId);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        Button uploadButton = (Button) findViewById(R.id.upload_image);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
//        ViewRecyclerAdapter adapter = new ViewRecyclerAdapter(this, streamList);
//        recyclerView.setAdapter(adapter);
    }

}

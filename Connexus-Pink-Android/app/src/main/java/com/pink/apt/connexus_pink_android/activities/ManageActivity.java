package com.pink.apt.connexus_pink_android.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;
import static com.pink.apt.connexus_pink_android.GlobalVars.MY_EMAIL;


import com.pink.apt.connexus_pink_android.R;

/**
 * Created by ari on 10/26/17.
 */

public class ManageActivity extends TabActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        // get email from intent
        Bundle extras = this.getIntent().getExtras();
        String email = extras.getString(Intent.EXTRA_TEXT);

        Button streamsButton = findViewById(R.id.manage_back_to_all_streams);
        streamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TabHost tabHost = findViewById(android.R.id.tabhost); // initiate TabHost
        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        Intent intent; // Reusable Intent for each tab

        spec = tabHost.newTabSpec("OwnedStreams"); // Create a new TabSpec using tab host
        spec.setIndicator("Owned Streams"); // set the “CONTACT” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, OwnedStreamActivity.class);
        intent.putExtra(intent.EXTRA_TEXT, email);
        spec.setContent(intent);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("SubscribedStreams"); // Create a new TabSpec using tab host
        spec.setIndicator("Subscribed Streams"); // set the “ABOUT” as an indicator
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent(this, SubscribedStreamsActivity.class);
        intent.putExtra(intent.EXTRA_TEXT, email);
        spec.setContent(intent);
        tabHost.addTab(spec);
        //set tab which one you want to open first time 0 or 1
        tabHost.setCurrentTab(0);
    }
}

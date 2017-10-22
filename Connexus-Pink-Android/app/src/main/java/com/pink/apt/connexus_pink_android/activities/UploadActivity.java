package com.pink.apt.connexus_pink_android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.EditText;

import com.pink.apt.connexus_pink_android.R;

/**
 * Created by ari on 10/22/17.
 */

public class UploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Set the upload edit text hint
        EditText uploadHint;
        uploadHint = (EditText) findViewById(R.id.uploadEditText);
        uploadHint.setHint("Add a message and/or tags");
        uploadHint.setGravity(Gravity.TOP);


        //TODO set stream name edit text
        EditText streamName = (EditText) findViewById(R.id.streamName);
        streamName.setEnabled(false);

    }
}

package com.pink.apt.connexus_pink_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        Button cameraButton = (Button) findViewById(R.id.uploadFromCameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });


        //TODO set stream name edit text
        TextView streamName = (TextView) findViewById(R.id.streamName);
        streamName.setEnabled(false);

    }
}

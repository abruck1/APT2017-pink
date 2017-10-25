package com.pink.apt.connexus_pink_android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pink.apt.connexus_pink_android.R;


public class UploadActivity extends AppCompatActivity {

    //request codes
    private static final int PICK_IMAGE = 1;
    private static final int USE_CAMERA = 2;
    private static final int UPLOAD = 3;

    private ImageView uploadImageView;
    private EditText tagsEditText;
    private Button uploadButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        //setup the controls
        tagsEditText = (EditText) findViewById(R.id.tagsEditText);
        uploadImageView = (ImageView) findViewById(R.id.uploadImageView);

        Button cameraButton = findViewById(R.id.uploadFromCameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });

        //setup the upload from library button
        Button libraryButton = (Button) findViewById(R.id.uploadFromLibraryButton);
        libraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        //setup the upload button
        uploadButton = (Button) findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent();
                // todo how to call startActivity to upload the image
            }
        });

        //TODO set stream name edit text, how to pass this in from stream page
        TextView streamName = (TextView) findViewById(R.id.streamName);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            //get the image URI
            Uri imageUri = data.getData();

            //display the selected image
            uploadImageView.setImageURI(imageUri);
            uploadImageView.setVisibility(View.VISIBLE);

            //enable the upload button
            uploadButton.setEnabled(true);

        }

        //todo add camera and upload code
    }

}

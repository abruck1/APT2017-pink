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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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
                //todo use StartActivityForResult and pass the USE_CAMERA resultcode
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

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE:
                    //get the upload URL here
                    RequestQueue queue = Volley.newRequestQueue(this);

                    //get the image URI
                    Uri imageUri = data.getData();

                    //display the selected image
                    uploadImageView.setImageURI(imageUri);
                    uploadImageView.setVisibility(View.VISIBLE);

                    //enable the upload button
                    uploadButton.setEnabled(true);

                    break;

                case USE_CAMERA:
                    //do stuff
                    break;

                case UPLOAD:
                    //do stuff
                    break;

                default:
                    //invalid resultCode
                    break;
            }
        }


//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="/getuploadurl";
//
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        mTextView.setText("Response is: "+ response.substring(0,500));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
//            }
//        });
//        // Add the request to the RequestQueue.
//        queue.add(stringRequest);
//


        //todo add camera and upload code
    }

}

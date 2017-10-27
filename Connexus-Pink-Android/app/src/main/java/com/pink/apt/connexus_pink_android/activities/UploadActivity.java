package com.pink.apt.connexus_pink_android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pink.apt.connexus_pink_android.R;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


import static com.pink.apt.connexus_pink_android.GlobalVars.GET_UPLOADURL_URL;


public class UploadActivity extends AppCompatActivity {
    private String TAG = "UploadActivity";

    //request codes
    private static final int PICK_IMAGE = 1;
    private static final int USE_CAMERA = 2;
    private static final int UPLOAD = 3;

    private ImageView uploadImageView;
    private EditText tagsEditText;
    private Button uploadButton;
    private String streamID;
    private String streamName;
    private String uploadURL;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // get info from intent
        Bundle extras = this.getIntent().getExtras();
        streamID = extras.getString("streamID");
        streamName = extras.getString("streamName");

        //setup the controls
        tagsEditText = (EditText) findViewById(R.id.tagsEditText);
        uploadImageView = (ImageView) findViewById(R.id.uploadImageView);

        Button cameraButton = findViewById(R.id.uploadFromCameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo how to get the image URI when this returns
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivityForResult(intent, USE_CAMERA);
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

                //try upload from here
                try {
                    UploadImage();
                    uploadImageView.setVisibility(View.INVISIBLE);
                    uploadButton.setEnabled(false);

                } catch (IOException e) {
                    Log.d(TAG, "uploadButton.onClick: " + e.toString());
                }

            }
        });

        TextView streamNameTextView = (TextView) findViewById(R.id.streamNameTextView);
        streamNameTextView.setText(streamNameTextView.getText() + streamName);

    }

    private void GetUploadURL() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_UPLOADURL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        uploadURL = response;
                        Log.d(TAG, "GetUploadURL onResponse: " + uploadURL);

                        //enable the upload button here to make sure this async call is complete
                        uploadButton.setEnabled(true);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "GetUploadURL onErrorResponse: " + error.toString());
                    }
                });
        Log.d(TAG, "request to this url:" + stringRequest);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void UploadImage() throws IOException {

        OkHttpClient client = new OkHttpClient();
        File file = new File(imageUri.getPath());

        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "image.jpg",
                        RequestBody.create(MediaType.parse("image/jpg"), file))
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder().url(uploadURL).post(formBody).build();

        okhttp3.Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        Log.d(TAG, "UploadImage: " + response.toString());


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "Activity result");
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "result ok requestCode=" + requestCode);
            switch (requestCode) {
                case PICK_IMAGE:
                    imageUri = data.getData();

                    //display the selected image
                    uploadImageView.setImageURI(imageUri);
                    uploadImageView.setVisibility(View.VISIBLE);

                    //get an upload URL, calling here since it is asynchronous
                    //the upload button is enabled in the callback
                    GetUploadURL();

                    break;

                case USE_CAMERA:
                    //todo will this be same code as above?
                    Log.d(TAG, "In use camera");
                    imageUri = data.getData();
                    Log.d(TAG, "image uri=" + imageUri);

                    //display the selected image
                    uploadImageView.setImageURI(imageUri);
                    uploadImageView.setVisibility(View.VISIBLE);

                    //get an upload URL, calling here since it is asynchronous
                    //the upload button is enabled in the callback
                    GetUploadURL();

                    break;

                case UPLOAD:
                    //do post
                    try {
                        UploadImage();
                    } catch (IOException e) {
                        Log.d(TAG, "onActivityResult: UPLOAD: " + e.toString());
                    }

                    break;

                default:
                    //invalid resultCode
                    break;
            }
        }

    }

}

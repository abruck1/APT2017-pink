package com.pink.apt.connexus_pink_android.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.*;
import com.pink.apt.connexus_pink_android.R;
import static com.pink.apt.connexus_pink_android.GlobalVars.GET_UPLOADURL_URL;
import cz.msebera.android.httpclient.Header;
import java.io.File;
import java.io.FileNotFoundException;



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
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // get info from intent
        Bundle extras = this.getIntent().getExtras();
        streamID = extras.getString("streamID");
        streamName = extras.getString("streamName");

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

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
                //try upload from here
                UploadImage();
                uploadImageView.setVisibility(View.INVISIBLE);
                uploadButton.setEnabled(false);
            }
        });

        TextView streamNameTextView = (TextView) findViewById(R.id.streamNameTextView);
        streamNameTextView.setText(streamNameTextView.getText() + streamName);

    }

    private void GetUploadURL() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_UPLOADURL_URL,
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

    public void UploadImage() {
        String filePath = GetFilePath(this, imageUri);
        File file = new File(filePath);
        Log.d("UploadActivity", "StreamID=" + streamID);
        Log.d("UploadActivity", "uploadURL=" + uploadURL.replace("/_ah", ""));
        Log.d("UploadActivity", "file name=" + filePath);

        RequestParams params = new RequestParams();
        try {
            params.setForceMultipartEntityContentType(true);
            params.put("file", file, "image/jpeg");
            params.put("streamID", streamID);
            params.put("latitude", 0);
            params.put("longitude", 0);
            //params.put("url", "");
            params.put("submit", "Submit");
//            params.put("redirect", "https://some.url.here");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // http://loopj.com/android-async-http/doc/com/loopj/android/http/AsyncHttpClient.html
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept-Encoding:", "gzip, deflate");
        client.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

        client.post(uploadURL, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] response, Throwable error) {
                String myResponse = new String(response);
                Log.d(TAG, "onFailure: " + myResponse);
            }
        });
    }

    public static String GetFilePath(Context context, Uri uri){
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "Activity result");
        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
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
                    Log.d(TAG, "In use camera" + data.getData());
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
                    //this isn't called
                    break;

                default:
                    //invalid resultCode
                    break;
            }
        } else{
            Log.d(TAG, "Either result code was error or data was null");
        }

    }

}

package com.pink.apt.connexus_pink_android;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import static com.pink.apt.connexus_pink_android.GlobalVars.*;

public class ViewAllStreams extends AppCompatActivity {

    private final String image_titles[] = {
            "Img1",
            "Img2",
            "Img3",
            "Img4",
            "Img5",
            "Img6",
            "Img7",
            "Img8",
            "Img1",
            "Img2",
            "Img3",
            "Img4",
            "Img5",
            "Img6",
            "Img7",
            "Img1",
            "Img2",
            "Img3",
            "Img4",
            "Img5",
            "Img6",
            "Img7",
            "Img1",
            "Img2",
            "Img3",
            "Img4",
            "Img5",
            "Img6",
            "Img7"
    };

    private final String imageUrls[] = {
            "https://imagejournal.org/wp-content/uploads/bb-plugin/cache/23466317216_b99485ba14_o-panorama.jpg",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "https://imagejournal.org/wp-content/uploads/bb-plugin/cache/23466317216_b99485ba14_o-panorama.jpg",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "https://imagejournal.org/wp-content/uploads/bb-plugin/cache/23466317216_b99485ba14_o-panorama.jpg",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "https://imagejournal.org/wp-content/uploads/bb-plugin/cache/23466317216_b99485ba14_o-panorama.jpg",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK"
    };

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_streams);
        queue = Volley.newRequestQueue(this);
        queue.start();


        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<CreateList> createLists = prepareData();
        MyAdapter adapter = new MyAdapter(this, createLists);
        recyclerView.setAdapter(adapter);

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.INTERNET)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET
                    );

            // MY_PERMISSIONS_REQUEST_INTERNET is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }



    private ArrayList<CreateList> prepareData(){
        MultiStreamParser msp = new MultiStreamParser(BASE_URL, queue);
        ArrayList<CreateList> theimage = new ArrayList<>();
        for(int i = 0; i< image_titles.length; i++){
            CreateList createList = new CreateList();
            createList.setImage_title(image_titles[i]);
            createList.setImage_ID(imageUrls[i]);
            theimage.add(createList);
        }
        return theimage;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}


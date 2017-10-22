package com.pink.apt.connexus_pink_android.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pink.apt.connexus_pink_android.CreateList;
import com.pink.apt.connexus_pink_android.MyAdapter;
import com.pink.apt.connexus_pink_android.R;

import java.util.ArrayList;

public class ViewStreamActivity extends AppCompatActivity {

    private final String image_titles[] = {
            "Img1",
            "Img2",
            "Img3",
            "Img4",
            "Img5",
            "Img6",
            "Img7",
            "Img8",
    };

    private final String imageUrls[] = {
            "https://imagejournal.org/wp-content/uploads/bb-plugin/cache/23466317216_b99485ba14_o-panorama.jpg",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK",
            "http://lh3.googleusercontent.com/La6izW4cf6HQJ5WweEm4FI93zSXGJ2qd58TGWhXL5dy3GlRt2Ng52Jaxg1LZ668Lpm5QGsH2o-42bxIXH5-RYBHK"
    };

    RequestQueue queue = Volley.newRequestQueue(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stream);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<CreateList> createLists = prepareData();
        MyAdapter adapter = new MyAdapter(this, createLists);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<CreateList> prepareData(){

        ArrayList<CreateList> theimage = new ArrayList<>();
        for(int i = 0; i< image_titles.length; i++){
            CreateList createList = new CreateList();
            createList.setImage_title(image_titles[i]);
            createList.setImage_ID(imageUrls[i]);
            theimage.add(createList);
        }
        return theimage;
    }
}

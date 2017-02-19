package com.pracainzynierska.geotagphotoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button OpenGallery;
    Button OpenMap;
    Button Close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OpenGallery = (Button) findViewById(R.id.open_gallery);
        OpenGallery.setOnClickListener(this);
        OpenMap = (Button) findViewById(R.id.open_map);
        OpenMap.setOnClickListener(this);
        Close = (Button) findViewById(R.id.close);
        Close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.open_gallery) {
            Intent start = new Intent(MainActivity.this, GalleryActivity.class);
            startActivity(start);
        }
        else if (v.getId() == R.id.open_map) {
            Intent start = new Intent(MainActivity.this, MapsActivity.class );
            startActivity(start);
        }
        else if (v.getId() == R.id.close) {
            finish();
            System.exit(0);
        }
    }


}

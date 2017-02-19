package com.pracainzynierska.geotagphotoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

public class FullScreenActivity extends AppCompatActivity {

    ImageView imageview;
    String filepath;
    private ArrayAdapter<String> mAdapter;
    EditText city;
    Button apply;
    public LocationAction locdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        Intent i = getIntent();
        filepath = i.getStringExtra("filepath");
        imageview = (ImageView) findViewById(R.id.imageview);

        File file = new File(filepath);
        Uri imageUri = Uri.fromFile(file);

        locdata = new LocationAction(this);

        Glide.with(this)
                .load(imageUri)
                .into(imageview);
        addDrawerItems();
    }

    private void addDrawerItems() {
        String[] osArray = { "Wyświetl informację o zdjęciu", "Usuń geotag", "Edytuj/Dodaj geotag" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        final ListView mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                                FullScreenActivity.this);

// Setting Dialog Title
                        alertDialog2.setTitle("Informacje o zdjęciu");

// Setting Dialog Message
                        alertDialog2.setMessage("Adres piku: " + filepath + "\n" +
                        "Szerokość geograficzna: " + locdata.ReadExifLat(filepath) + "\n" +
                        "Długość geograficzna: " + locdata.ReadExifLng(filepath));

// Setting Positive "Yes" Btn
                        alertDialog2.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to execute after dialog
                                    }
                                });

// Showing Alert Dialog
                        alertDialog2.show();
                        break;
                    case 1:
                        locdata.SaveExifData(filepath, 0, 0);
                        break;
                    case 2:
                    {
                        city = (EditText)findViewById(R.id.etcity);
                        city.setVisibility(View.VISIBLE);
                        apply = (Button)findViewById(R.id.apply);
                        apply.setVisibility(View.VISIBLE);
                        apply.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                double latitude = locdata.getLatFromAddress(city.getText().toString());
                                double longitude = locdata.getLngFromAddress(city.getText().toString());
                                locdata.SaveExifData(filepath, latitude, longitude);
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Udało się!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    }
                }
            }
        });
    }
}

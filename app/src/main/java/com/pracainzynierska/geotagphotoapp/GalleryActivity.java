package com.pracainzynierska.geotagphotoapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import com.bumptech.glide.Glide;


public class GalleryActivity extends AppCompatActivity {

    private ArrayList<String> images;
    Button edit;
    Button apply;
    int count = 0;
    EditText city;
    public LocationAction locdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        GridView gallery = (GridView) findViewById(R.id.galleryGridView);
        gallery.setAdapter(new ImageAdapter(this));

        edit = (Button) findViewById(R.id.edit);
        apply = (Button) findViewById(R.id.apply);
        locdata = new LocationAction(this);

        final ArrayList<String> checklist = new ArrayList<String>();
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                ImageView imageView = (ImageView) arg1;
                imageView.setImageResource(R.drawable.ic_launcher);
                checklist.add(images.get(position));

                Toast.makeText(GalleryActivity.this,
                        "Dodano do edycji" ,
                        Toast.LENGTH_SHORT).show();
                count++;
            }
        });

        gallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                Intent i = new Intent(GalleryActivity.this, FullScreenActivity.class);
                i.putExtra("filepath", images.get(position));
                i.putExtra("position", position);
                startActivity(i);
                return true;
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                city = (EditText)findViewById(R.id.etcity);
                city.setVisibility(View.VISIBLE);
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] check = new String[checklist.size()];
                check = checklist.toArray(check);
                for (int i=0; i<check.length; i++)
                {
                    double latitude = locdata.getLatFromAddress(city.getText().toString());
                    double longitude = locdata.getLngFromAddress(city.getText().toString());
                    locdata.SaveExifData(check[i], latitude, longitude);
                    /*Toast.makeText(GalleryActivity.this,
                            "plik " + i + " \nadres: " + check[i] ,
                            Toast.LENGTH_LONG).show();*/
                }
            }
        });
    }

    private class ImageAdapter extends BaseAdapter {

        /** The context. */
        private Activity context;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext
         *            the local context
         */
        public ImageAdapter(Activity localContext) {
            context = localContext;
            images = getAllImagesPath(context);
                    }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ImageView picturesView;
            if (convertView == null) {
                picturesView = new ImageView(context);
                picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                picturesView
                        .setLayoutParams(new GridView.LayoutParams(270, 270));

            } else {
                picturesView = (ImageView) convertView;
            }

            Glide.with(context).load(images.get(position))
                    .placeholder(R.drawable.ic_launcher).centerCrop()
                    .into(picturesView);

            return picturesView;
        }

        /**
         * Getting All Images Path.
         *
         * @param activity
         *            the activity
         * @return ArrayList with images Path\
         */
        private ArrayList<String> getAllImagesPath(Activity activity) {
            Uri uri;
            Cursor cursor;
            int column_index_data;
            ArrayList<String> listOfAllImages = new ArrayList<String>();
            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

            cursor = activity.getContentResolver().query(uri, projection, null,
                    null, null);

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);

                listOfAllImages.add(absolutePathOfImage);
            }
            return listOfAllImages;
        }
    }
}

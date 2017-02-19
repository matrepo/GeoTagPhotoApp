package com.pracainzynierska.geotagphotoapp;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private NfcAdapter nfcAdapter;
    private GoogleMap mMap;
    private ArrayList<String> images;
    public LocationAction locdata;
    Button ShareMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locdata = new LocationAction(this);
        ShareMap = (Button) findViewById(R.id.share_map);
        ShareMap.setOnClickListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        images = getAllImagesPath(this);
        String[] arr = new String[images.size()];
        arr = images.toArray(arr);
        for (int i=1; i<=51; i++)
        {
            if (locdata.ReadExifLat(arr[i]) != 0)
            {
                mMap.addMarker(new MarkerOptions().
                        position(new LatLng(locdata.ReadExifLat(arr[i]),locdata.ReadExifLng(arr[i]))).
                        title(arr[i]));
            }
        }
        sendFile();
    }

    private ArrayList<String> getAllImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
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

    public void CaptureMapScreen()
    {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                // TODO Auto-generated method stub
                bitmap = snapshot;
                try {
                    FileOutputStream out = new FileOutputStream("/mnt/sdcard/"
                            + "MyMapScreen"
                            + ".jpeg");

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mMap.snapshot(callback);
    }

    public void sendFile() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

            File fileToTransfer = new File("/storage/emulated/0/MyMapScreen.jpeg");

            nfcAdapter.setBeamPushUris(new Uri[]{Uri.fromFile(fileToTransfer)}, this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.share_map) {
            CaptureMapScreen();
        }
    }
}

package com.pracainzynierska.geotagphotoapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mateusz on 2017-01-09.
 */

public class LocationAction extends ContextWrapper{

    public LocationAction(Context base) {
        super(base);
    }

    String ReadExif(String file){
        String exif="";
        try {
            ExifInterface exifInterface = new ExifInterface(file);

            if (exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)!=null) {
                exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                exif += "\n TAG_GPS_LONGITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            }
            else
            {
                exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
                exif += "\n TAG_GPS_LONGITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this,
                    e.toString(),
                    Toast.LENGTH_LONG).show();
        }

        return exif;
    }

    Double ReadExifLat(String file){
        String exif="";
        try {
            ExifInterface exifInterface = new ExifInterface(file);

            if (exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)!=null) {
                exif +=ExifConvert(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE));
            }
            else
            {
                exif +="0";
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this,
                    e.toString(),
                    Toast.LENGTH_LONG).show();
        }

        return Double.valueOf(exif);
    }

    Double ReadExifLng(String file){
        String exif="";
        try {
            ExifInterface exifInterface = new ExifInterface(file);

            if (exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE)!=null) {
                exif +=ExifConvert(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            }
            else
            {
                exif +="0";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this,
                    e.toString(),
                    Toast.LENGTH_LONG).show();
        }

        return Double.valueOf(exif);
    }

    void SaveExifData(String file, double lat, double lng){
        if (lat!=0)
        {
            try {
                ExifInterface exifInterface = new ExifInterface(file);
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, ExifConvertBack(lat));
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, ExifConvertBack(lng));
                exifInterface.saveAttributes();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(this,
                        e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            try {
                ExifInterface exifInterface = new ExifInterface(file);
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LATITUDE, "0/1,0/1,0/1");
                exifInterface.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, "0/1,0/1,0/1");
                exifInterface.saveAttributes();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText(this,
                        e.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    Double ExifConvert (String data) {
        String tab[] = null;
        tab = data.split(",");
        String[] var1;
        String[] var2;
        String[] var3;
        var1 = tab[0].split("/");
        var2 = tab[1].split("/");
        var3 = tab[2].split("/");
        double result;
        result  = Double.valueOf(var1[0])/Double.valueOf(var1[1])
                +Double.valueOf(var2[0])/(Double.valueOf(var2[1])*60)
                +Double.valueOf(var3[0])/(Double.valueOf(var3[1])*3600);
        return result;
    }

    String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    double getLatFromAddress(String Address) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> address;
        double lat = 0;
        try {
            address = geocoder.getFromLocationName(Address, 5);
            if (address == null) {
                return Double.parseDouble(null);
            }
            Address location = address.get(0);

            lat = (double) (location.getLatitude());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lat;
    }

    double getLngFromAddress(String Address) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> address;
        double lng = 0;
        try {
            address = geocoder.getFromLocationName(Address, 5);
            if (address == null) {
                return Double.parseDouble(null);
            }
            Address location = address.get(0);

            lng = (double) (location.getLongitude());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lng;
    }

    String ExifConvertBack (double data)
    {
        StringBuilder sb = new StringBuilder(20);
        data=Math.abs(data);
        int degree = (int) data;
        data *= 60;
        data -= (degree * 60.0d);
        int minute = (int) data;
        data *= 60;
        data -= (minute * 60.0d);
        int second = (int) (data*1000.0d);

        sb.setLength(0);
        sb.append(degree);
        sb.append("/1,");
        sb.append(minute);
        sb.append("/1,");
        sb.append(second);
        sb.append("/1000,");
        return sb.toString();
    }
}

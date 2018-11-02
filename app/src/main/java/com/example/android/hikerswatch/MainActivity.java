package com.example.android.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;

    LocationListener locationListener;

    TextView latitude;

    TextView longitude;

    TextView accuracy;

    TextView altitude;

    TextView addressText;
    String address;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startListening();
        }
    }

    public  void startListening()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {

            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        }
    }

    public void updateLocationInfo(Location location) throws IOException {

        Log.i("Location of me",location.toString());

        latitude=(TextView)findViewById(R.id.latitude);

        longitude=(TextView)findViewById(R.id.longitude);

        accuracy=(TextView)findViewById(R.id.accuracy);

        altitude=(TextView)findViewById(R.id.altitude);

        addressText = (TextView)findViewById(R.id.address);

        latitude.setText("Latitude  : "+Double.toString(location.getLatitude()));

        longitude.setText("Longitude : "+Double.toString(location.getLongitude()));

        accuracy.setText("Accuracy : "+Double.toString(location.getAccuracy()));

        altitude.setText("Altitude : "+Double.toString(location.getAltitude()));

        Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());

        try {

             address="Could not find Address";

            List<Address> list =geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            if(list!=null&&list.size()>0) {

                Log.i("Place Info", list.get(0).toString());

                address="";

                if(list.get(0).getSubThoroughfare()!=null)
                {
                    address+= list.get(0).getSubThoroughfare()+" ";
                }

                if(list.get(0).getThoroughfare()!=null)
                {
                    address+= list.get(0).getThoroughfare()+"\n";
                }

                if(list.get(0).getLocality()!=null)
                {
                    address+= list.get(0).getLocality()+"\n";
                }

                if(list.get(0).getPostalCode()!=null)
                {
                    address+= list.get(0).getPostalCode()+"\n";
                }

                if(list.get(0).getCountryName()!=null)
                {
                    address+= list.get(0).getCountryName()+"\n";
                }
            }

            addressText.setText("Address : "+ address);

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                try {
                    updateLocationInfo(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (Build.VERSION.SDK_INT < 23) {

           startListening();

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                 Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                 if(location!=null) {
                     try {
                         updateLocationInfo(location);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
            }
        }
    }
}

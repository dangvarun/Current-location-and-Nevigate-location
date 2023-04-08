package com.gamingrea.map_application;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("ALL")

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Button btn;
    TextView txt;
    String address;
    private FusedLocationProviderClient fusedLocationClient;
    double latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        btn = findViewById(R.id.button);
        txt = findViewById(R.id.textView);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MapsActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationfind();
                } else {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
    }


    private void locationfind() {
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude,longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng).title("current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));




                    //mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

                    Geocoder gc = new Geocoder(MapsActivity.this);
                    if(gc.isPresent()) {
                        List<Address> list = null;
                        try {
                            list = gc.getFromLocation(latitude, longitude, 1);
                        } catch (IOException e) {
e.printStackTrace();
                        }
                        Address address = list.get(0);
                        StringBuffer str = new StringBuffer();
                        str.append("name" + address.getLocality().toString() + "\n");
                        str.append("sub-Admin Area" + address.getSubAdminArea() + "\n");
                        str.append("Admin Area" + address.getAdminArea() + "\n");


                        String strAddress = str.toString();


                        txt.setText("Latitude = " + latitude + "\n" +
                                "Longitude =" + longitude + "address" + strAddress);
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*Toast.makeText(this, "Latitude = " + latitude + "\n" +
                "Longitude =" + longitude, Toast.LENGTH_SHORT).show();*/
        // LatLng latLng = new LatLng(latitude,longitude);
        // mMap.addMarker(new MarkerOptions().position(latLng).title("i'm here"));
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }
}
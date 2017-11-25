package com.thanh.android.polyhackathon;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {



    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Button btnCheckin;
    TextView txtLocation;
    Location Mylocation;
    Double MyLocationAltitude;
    LatLng latLngPoly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        else {
            mapFragment.getMapAsync(MainActivity.this);
        }
        initControls();
        initDisplays();
        initEvents();
    }

    private void initEvents() {
        btnCheckin.setOnClickListener(this);
    }

    private void initDisplays() {

    }

    private void initControls() {
        //mapped
        btnCheckin = (Button) findViewById(R.id.button_checkin);
        txtLocation = (TextView) findViewById(R.id.textView_Location);
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
    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        LatLng home = new LatLng(10.774417, 106.636504);
//        mMap.addMarker(new MarkerOptions().position(home).title("My home"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 17));

        // map type
//        mMap.setMapType(googleMap.MAP_TYPE_NORMAL);//binh thuong
//        mMap.setMapType(googleMap.MAP_TYPE_TERRAIN);
//        mMap.setMapType(googleMap.MAP_TYPE_HYBRID);// Ve tinh co thong tin
//        mMap.setMapType(googleMap.MAP_TYPE_SATELLITE); // ve tinh khong co thong tin

        // vi tri hien tai
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    mapFragment.getMapAsync(MainActivity.this);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_checkin:
//                txtLocation.setText(Mylocation.getAltitude()+"");
                break;
        }
    }
}
package com.thanh.android.polyhackathon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.graphics.*;
import android.widget.*;
import android.provider.*;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {


    private final int PICK_IMAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    Button btnCheckin;
    TextView txtLocation, txtSchoolLocation, txtStudentStatus;
    Location mylocation;
    Address addressMyLocation;
    double myLocationLatitude;
    double myLocationLngtitude;
    double SchoolLocationLatitude;
    double SchoolLocationLngtitude;
    LatLng latLngMyLocation;
    LatLng latLngSchoolLocation;
    EditText edtLocation;
    ProgressBar progressLoad;
    SeekBar seekBar;
    private Circle mCircle;
    private Marker mMarker;
    Double distanceToSchool;
    Boolean firstOpentApp = true;
    ImageView imgPhoto;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("School");


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
        } else {
            mapFragment.getMapAsync(MainActivity.this);
        }

        initControls();
        initDisplays();
        initEvents();
        initFace();
    }

    private void initFace() {

    }

    private void initEvents() {
        btnCheckin.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtLocation.setText(i + " ");
//                if (mCircle == null || mMarker == null) {
//                    drawMarkerWithCircle(i, latLngMyLocation);
//                } else {
//                    updateMarkerWithCircle(latLngMyLocation);
//                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getLatLngSchoolData();
    }


    private void getLatLngSchoolData() {
        DatabaseReference databaseReference = mDatabase.child("Fpoly");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (mMarker != null) {
                        mMarker.remove();
                    }

                    double Lat = dataSnapshot.child("Lat").getValue(Double.class);
                    double Lng = dataSnapshot.child("Long").getValue(Double.class);
                    setSchoolLatLng(Lat, Lng);
                    mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(SchoolLocationLatitude, SchoolLocationLngtitude)));
                    StudentStatus();
                    Log.d("firebase", SchoolLocationLatitude + " " + SchoolLocationLngtitude);
                } catch (Exception e) {
                    Log.d("data", e + "");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initDisplays() {


        txtLocation.setVisibility(View.GONE);
        txtSchoolLocation.setVisibility(View.GONE);
        txtSchoolLocation.setText("School location \n" + SchoolLocationLatitude + " " + SchoolLocationLngtitude);
        txtStudentStatus.setText(distanceTwoPoin(latLngMyLocation, latLngSchoolLocation) + "");
    }

    private void initControls() {
        //mapped
        btnCheckin = (Button) findViewById(R.id.button_checkin);
        txtLocation = (TextView) findViewById(R.id.textView_Location);
        txtSchoolLocation = (TextView) findViewById(R.id.textView_LocationSchool);
        txtStudentStatus = (TextView) findViewById(R.id.textView_studentStatus);
        edtLocation = (EditText) findViewById(R.id.edtLocation);
        progressLoad = (ProgressBar) findViewById(R.id.process_main);
        seekBar = (SeekBar) findViewById(R.id.seekBar_metter);

        imgPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
        getLatLngSchoolData();

        latLngSchoolLocation = new LatLng(SchoolLocationLatitude, SchoolLocationLngtitude);
        findViewById(R.id.btn_test_logout).setOnClickListener(this);
        if (latLngMyLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngMyLocation));
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    private void setSchoolLatLng(double Lat, double Lng) {
        SchoolLocationLatitude = Lat;
        SchoolLocationLngtitude = Lng;
        latLngSchoolLocation = new LatLng(SchoolLocationLatitude, SchoolLocationLngtitude);

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
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mylocation = location;
                myLocationLatitude = location.getLatitude();
                myLocationLngtitude = location.getLongitude();
                latLngMyLocation = new LatLng(myLocationLatitude, myLocationLngtitude);
                txtLocation.setText(myLocationLatitude + " " + myLocationLngtitude);
////                seekBar.setVisibility(View.VISIBLE);
//                if (mCircle == null || mMarker == null) {
//                    drawMarkerWithCircle(50, latLngMyLocation);
//                } else {
//                    updateMarkerWithCircle(latLngMyLocation);
//                }
                txtStudentStatus.setText(StudentStatus());
                if (firstOpentApp) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngMyLocation, 15));
                    firstOpentApp = false;
                }
                if (SchoolLocationLngtitude != 0.0) {
                    mMarker.remove();
                    mMarker = mMap.addMarker(new MarkerOptions().position(latLngSchoolLocation));
                }
            }
        });


//        latLngPoly = new LatLng(10.79085214, 106.68211162);
//        mMap.addMarker(new MarkerOptions().position(latLngPoly).title("poly hcm"));
//        mMap.setOnMyLocationClickListener(myLocationClickListener);
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
        switch (view.getId()) {
            case R.id.button_checkin:
                Intent intent1 = new Intent(getApplicationContext(), ScanActivity.class);
                startActivity(intent1);
//                GetImageInGallery();
//                Toast.makeText(this, Lat, Toast.LENGTH_SHORT).show();
//                String adr = txtLocation.getText().toString().trim();
//                setMarker(adr);
                break;
            case R.id.btn_test_logout:

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("signout", true);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }


    private void updateMarkerWithCircle(LatLng position) {
        mCircle.setCenter(position);
        mMarker.setPosition(position);
    }

//    private void drawMarkerWithCircle(double RadiusInMeters, LatLng position) {
//        double radiusInMeters = RadiusInMeters;
//        int strokeColor = 0xffff0000; //red outline
//        int shadeColor = 0x44ff0000; //opaque red fill
//
//        CircleOptions circleOptions = new CircleOptions().center(position).radius(radiusInMeters).fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
//        mCircle = mMap.addCircle(circleOptions);
//
//        MarkerOptions markerOptions = new MarkerOptions().position(position);
//        mMarker = mMap.addMarker(markerOptions);
//    }

//    private void setMarker(String Adress) {
//        progressLoad.setVisibility(View.VISIBLE);
//        String adress = Adress;
//        try {
//            LatLng latLngAddress = getLocationFromAddress(getApplicationContext(), adress);
//
//            mMap.addMarker(new MarkerOptions().position(latLngAddress));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngAddress, 10));
//
//        } catch (Exception e) {
//            Log.d("LOCATION", e + "");
//        } finally {
//            progressLoad.setVisibility(View.GONE);
//        }
//    }

//    public LatLng getLocationFromAddress(Context context, String strAddress) {
//
//        Geocoder coder = new Geocoder(context);
//        List<Address> address;
//        LatLng p1 = null;
//
//        try {
//            // May throw an IOException
//            address = coder.getFromLocationName(strAddress, 5);
//            if (address == null) {
//                return null;
//            }
//            Address location = address.get(0);
//            location.getLatitude();
//            location.getLongitude();
//
//            p1 = new LatLng(location.getLatitude(), location.getLongitude());
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return p1;
//    }

    private String StudentStatus() {
        if (distanceTwoPoin(latLngMyLocation, latLngSchoolLocation) < 65.0) {
            btnCheckin.setEnabled(true);
            return "Đang ở trường";

        } else {
            btnCheckin.setEnabled(false);
            return "Đang không ở trường, không được check in \n" + "cách trường : " + distanceToSchool + " metter";
        }


    }

//    private void AddMakerWithLatLng(double latitude, double longtitude) {
//        try {
//            LatLng latLng = new LatLng(latitude, longtitude);
//            mMap.addMarker(new MarkerOptions().position(latLng));
//        }catch (Exception e){
//            Log.d("location", "add maker " + e);
//        }
//    }

    public double distanceTwoPoin(LatLng p1, LatLng p2) {
        if (p1 == null) return -1;
        double R = 6378137.0;
        Log.d("location", "p2 la " + p2.latitude);

        Log.d("location", "p1 la " + p1.latitude);
        double dLat = rad(p2.latitude - p1.latitude);
        double dLng = rad(p2.longitude - p1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rad(p1.latitude)) * Math.cos(rad(p2.latitude)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        Log.d("location", "distance " + d);
        d = Math.round(d);
        distanceToSchool = d;
        return d;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE
//                && resultCode == RESULT_OK
//                && data != null
//                && data.getData() != null){
//            //get link image in device
//            Uri uri = data.getData();
//            try {
//                //get bitmap
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                //set image and show in screen
//                imgPhoto.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgPhoto.setImageBitmap(imageBitmap);
        }
    }

    public Double rad(double x) {
        return x * Math.PI / 180;
    }

//    private void GetImageInGallery() {
//        Intent intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
//        intentGallery.setType("image/*");
//        startActivityForResult(Intent.createChooser(intentGallery,
//                "Select Picture"),
//                PICK_IMAGE);
//    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}
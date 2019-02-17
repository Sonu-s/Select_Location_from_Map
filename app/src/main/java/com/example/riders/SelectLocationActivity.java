package com.example.riders;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener {

    private SupportMapFragment mapFrag;
    private Marker mCurrLocationMarker;
    private GoogleMap mGoogleMap;
    private ImageView iconView;
    private LatLng center;
    private TextView fullAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        iconView = findViewById(R.id.iconView);
        fullAddress = findViewById(R.id.fullAddress);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(MainActivity.mLastLocation.getLatitude(), MainActivity.mLastLocation.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));

        // Move Camera...

        mGoogleMap.setOnCameraMoveStartedListener(this);
        mGoogleMap.setOnCameraMoveListener(this);
        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnCameraMoveCanceledListener(this);

    }

    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {

            Log.i("_Camera","The user gestured on the map."+ reason);


        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {

            Log.i("_Camera","The user tapped something on the map.");

        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {

            Log.i("_Camera","The app moved the camera..");
        }
    }

    @Override
    public void onCameraMove() {

       // Log.i("_Camera","The camera is moving.");
        LatLng center = mGoogleMap.getCameraPosition().target;
       // Log.i("_Camera",center.latitude +"--" +center.longitude);
    }

    @Override
    public void onCameraMoveCanceled() {

        Log.i("_Camera","Camera movement canceled.");
    }

    @Override
    public void onCameraIdle() {

        Log.i("_Camera","The camera has stopped moving.");
         center = mGoogleMap.getCameraPosition().target;
        Log.i("_Camerafinal",center.latitude +"--" +center.longitude);

        getAddress(center.latitude,center.longitude);
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(SelectLocationActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
//            add = add + "_" + obj.getCountryName();
//            add = add + "_" + obj.getCountryCode();
//            add = add + "_" + obj.getAdminArea();
//            add = add + "_" + obj.getPostalCode();
//            add = add + "_" + obj.getSubAdminArea();
//            add = add + "_" + obj.getLocality();
//            add = add + "_" + obj.getSubThoroughfare();

            Log.i("_CameraLocation",add);

            fullAddress.setText(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}


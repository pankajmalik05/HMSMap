package com.demoapp.hsmmap;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;

import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.CameraPosition;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.LatLng;


/**
 * map activity entrance class
 */

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationServiceCallback,GeoCodingServiceCallback {

    //Huawei map
    private HuaweiMap hMap;
    private MapView mMapView;
    private LatLng latLng;
    private Marker mMarker;
    private Marker pMarker;

    private final String[] RUNTIME_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
    };

    private final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!PermissionService.hasPermissions(this, RUNTIME_PERMISSIONS)) {
            PermissionService.requestPermissions(this,RUNTIME_PERMISSIONS);
        }

        //get mapview instance
        mMapView = findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        //get map instance
        mMapView.getMapAsync(this);

        MyRequestQueue.createQueue(this);
        LocationService.getCurrentLocation(this,this);

    }

    @Override
    public void onMapReady(HuaweiMap map) {
        //get map instance in a callback method
        hMap = map;
        hMap.setMyLocationEnabled(false);// Enable the my-location overlay.

        hMap.setOnMapClickListener(new HuaweiMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setPinLocation(latLng);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle)
        ;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }



    // callback for Location service returning current location
    @Override
    public void onGetLocation(Location location){
        latLng = new LatLng(location.getLatitude(),location.getLongitude());
        setCurrentLocation();
        GeoCodingService.getLocationFromCords(location.getLatitude(),location.getLongitude(),"currentLocation",this);
    }

    // callback for GeoCoding service returning location description
    @Override
    public void onGetLocationDescription(String location,String description){

        switch (description)
        {
            case "currentLocation":
                setCurrentLocationDescription(location);
                break;

            case "pinLocation":
                setPinDescription(location);
                break;

            default:
                // default action for returned location
                break;
        }

    }

    // setting Address for the dropped pin
    private void setPinDescription(String location)
    {
        pMarker.setTitle(location);
        pMarker.showInfoWindow();
    }

    // setting Address for the current location
    private  void setCurrentLocationDescription(String location){
        mMarker.setTitle(location);
    }

    // setting current location
    private void setCurrentLocation( ){

        // move camera by CameraPosition param
        CameraPosition build = new CameraPosition.Builder().target(latLng).zoom(15).build();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(build);
        hMap.animateCamera(cameraUpdate);
        hMap.setMaxZoomPreference(25);
        hMap.setMinZoomPreference(1);

        mMarker = hMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.star))
                .anchor(0.5f,0.5f)
                .clusterable(true));

       // mMarker.showInfoWindow();
    }

    // setting pin location
    private void setPinLocation(LatLng latLng){
        if (pMarker == null)
        {
            pMarker = hMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                    .anchor(0.5f, 1.0f)
                    .clusterable(true));
        }
        else
        {
            pMarker.setPosition(latLng);
        }
        GeoCodingService.getLocationFromCords(latLng.latitude,latLng.longitude,"pinLocation",this);
    }



}

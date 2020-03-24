package com.demoapp.hsmmap;

import android.app.Activity;
import android.location.Location;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationServices;


class LocationService {


    static void getCurrentLocation(Activity activity, final LocationServiceCallback callback)
    {
        //create a fusedLocationProviderClient
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

         fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(final Location location) {
                        if (location == null) {
                            return;
                        }
                        //Returning Location to the callback.
                        callback.onGetLocation(location);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        //Exception handling logic.
                    }
                });

    }
}

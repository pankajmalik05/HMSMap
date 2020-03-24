package com.demoapp.hsmmap;

import android.location.Location;

public interface GeoCodingServiceCallback {

    void onGetLocationDescription(String location , String description);

}
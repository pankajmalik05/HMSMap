package com.demoapp.hsmmap;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyRequestQueue {

    private static RequestQueue MyRequestQueue;

    public static RequestQueue getQueue()
    {
        return MyRequestQueue;
    }

    public static void createQueue(Context context){
        MyRequestQueue = Volley.newRequestQueue(context);
    }
}

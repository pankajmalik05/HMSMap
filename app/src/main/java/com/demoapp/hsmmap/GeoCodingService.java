package com.demoapp.hsmmap;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


 class GeoCodingService {

     private final static String url = "https://siteapi.cloud.huawei.com/mapApi/v1/siteService/reverseGeocode?key=CV6RrVQBUYAKBI%2B9SlsXgQXTlRO%2Fs%2Ff35Q5wtnNVVvWre8gyyM3zhqUUtmjzExdQrZYCgKWGN7FxjLrykpD48whvf6EA";

     static void getLocationFromCords(double lattitude , double longitude, final String description, final GeoCodingServiceCallback callback)
        {
            try {
                MyRequestQueue.getQueue().cancelAll(description);

                JSONObject locationObj = new JSONObject();
                locationObj.put("lng", longitude);
                locationObj.put("lat", lattitude);

                JSONObject postparams = new JSONObject();
                postparams.put("location", locationObj);
                postparams.put("language", "en");
                postparams.put("politicalView", "CN");

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, postparams,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try{
                                    String curLocation = response.getJSONArray("sites").getJSONObject(0).getString("formatAddress");

                                    callback.onGetLocationDescription(curLocation,description);
                                }
                                catch (Exception ex){
                                    ex.printStackTrace();
                                    callback.onGetLocationDescription("Blank","Blank");
                                }

                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Failure Callback
                            }
                        })
                {
                    // Passing some request headers
                    @Override
                    public Map<String,String> getHeaders() throws AuthFailureError {
                        HashMap headers = new HashMap();
                        headers.put("Content-Type", "application/json");
                        headers.put("Accept", "application/json");
                        return headers;
                    }
                };

                jsonObjReq.setTag(description);
                MyRequestQueue.getQueue().add(jsonObjReq);

            }
            catch (Exception ex){
                ex.printStackTrace();
            }



        }
}

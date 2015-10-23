package com.gpsapp.tracker.api;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by andredelgado on 23/10/15.
 */
public class RestClientUsage {
    public static void sendCoordinates(RequestParams params, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        RestClient.post("coordinates", params, asyncHttpResponseHandler);
    }

    public static void sendSteps(RequestParams params, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        RestClient.post("steps", params, asyncHttpResponseHandler);
    }
}

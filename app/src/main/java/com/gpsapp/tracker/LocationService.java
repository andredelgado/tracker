package com.gpsapp.tracker;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.gpsapp.tracker.api.RestClientUsage;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

public class LocationService extends Service {

    private enum State {
        IDLE, WORKING;
    }

    private static State state;


    static {
        state = State.IDLE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent i = new Intent();
        i.setAction("TimeToUpdate");
        MainActivity.getAppContext().sendBroadcast(i);
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (state == State.IDLE) {
            state = State.WORKING;
            ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(MainActivity.getAppContext());
            locationProvider.getLastKnownLocation()
                    .subscribe(new Action1<Location>() {
                        @Override
                        public void call(Location location) {
                            RequestParams params = new RequestParams();
                            params.put("latitude", location.getLatitude());
                            params.put("longitude", location.getLongitude());
                            RestClientUsage.sendCoordinates(params, new JsonHttpResponseHandler() {
                                @TargetApi(Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    Log.d("LocationService", response.toString());
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                    Log.d("LocationService", errorResponse.toString());
                                }
                            });
                            Log.d("LastLocation", String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()));
                            MainActivity.getInstance().updateTextView(String.valueOf(location.getLatitude()) + String.valueOf(location.getLongitude()));
                            stopSelf();
                        }
                    });
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        state = State.IDLE;
    }
}

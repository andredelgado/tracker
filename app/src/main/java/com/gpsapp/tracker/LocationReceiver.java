package com.gpsapp.tracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by andredelgado on 22/10/15.
 */
public class LocationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent calledIntent)
    {

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final PendingIntent wakeupIntent = PendingIntent.getService(context, 0,
                new Intent(context, LocationService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        final boolean hasNetwork = !calledIntent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (hasNetwork) {
            // start service now for doing once
            context.startService(new Intent(context, LocationService.class));
            Log.d("Passei Aqui", "Passei Aqui");
            //context.stopService(new Intent(context, LocationService.class));
            // schedule service for every 15 minutes
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 60000,
                    60000, wakeupIntent);
        } else {
            alarmManager.cancel(wakeupIntent);
        }

    }

}

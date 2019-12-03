package app.com.friendstracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import app.com.locationtracker.LocationTracker;


public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent trackerIntent = new Intent(context, LocationTracker.class);
        context.startService(trackerIntent);
    }
}

package app.com.locationtracker;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class LocationTracker extends Service implements LocationListener {
        private LocationManager locationManager;
        private static final String TAG = "LocationTracker";
        private static TrackerSettings trackerSetting;
        private boolean mIsListening = false;
        private Looper mServiceLooper;
        private ServiceHandler mServiceHandler;

        public class ServiceHandler extends Handler {
            public ServiceHandler(Looper looper) {
                super(looper);
            }

            @Override
            public void handleMessage(Message msg) throws SecurityException {
                super.handleMessage(msg);
                int startId = msg.arg1;
                while(true){
                    Location location = null;
                    startTracking();
                    while (location == null ) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (location == null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                        if (location == null) {
                            location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                        }
                        if (location != null) {
                            if (!location.hasAccuracy() || location.getAccuracy() > trackerSetting.getMinAccuracyToupdate()) {
                                location = null;
                            }
                        }

                    }

                    Log.d(TAG, location.toString());
                    //mServiceHandler.post(new MakeToast(location.toString()));
                    stopTracking();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Do some processing
                //boolean stopped = stopSelfResult(startId);
                //LocationTracker.reScheduleService(getApplicationContext());
            }
        }
        @Override
        public void onCreate() {
            super.onCreate();
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            LocationTracker.trackerSetting = LocationTracker.trackerSetting==null? TrackerSettings.DEFAULT_SETTINGS: LocationTracker.trackerSetting;
            // Start up the thread running the service.  Note that we create a
            // separate thread because the service normally runs in the process's
            // main thread, which we don't want to block.  We also make it
            // background priority so CPU-intensive work will not disrupt our UI.
            HandlerThread thread = new HandlerThread("ServiceStartArguments",
                    Process.THREAD_PRIORITY_BACKGROUND);
            thread.start();
            stopTracking();
            // Get the HandlerThread's Looper and use it for our Handler
            mServiceLooper = thread.getLooper();
            mServiceHandler = new ServiceHandler(mServiceLooper);
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            // For each start request, send a message to start a job and deliver the
            // start ID so we know which request we're stopping when we finish the job
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            mServiceHandler.sendMessage(msg);


            // If we get killed, after returning from here, restart
            return START_STICKY;
        }

        private void startTracking() {
            try {
                Log.i(TAG, "LocationTracked has started listening for location updates");
                mIsListening = true;

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, LocationTracker.trackerSetting.getMinDistanceToUpdate(), this);

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, LocationTracker.trackerSetting.getMinDistanceToUpdate(), this);

                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, LocationTracker.trackerSetting.getMinDistanceToUpdate(), this);
            } catch (SecurityException e) {
                e.printStackTrace();
                Log.w(TAG, "Location permission not granted");
            }
        }

        private final void stopTracking() {
            if (mIsListening) {
                Log.i(TAG, "LocationTracked has stopped listening for location updates");
                locationManager.removeUpdates(this);
                mIsListening = false;
            } else {
                Log.i(TAG, "LocationTracked wasn't listening for location updates anyway");
            }
        }

        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            stopTracking();
        }

        private class MakeToast implements Runnable{
            private String text;

            public MakeToast(String string){
                this.text = string;
            }
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        }
        public static void start(Context context, TrackerSettings settings) {

            LocationTracker.trackerSetting = settings;
            Intent serviceIntent = new Intent(context, LocationTracker.class);
            context.startService(serviceIntent);
        }

        private static void reScheduleService(Context context) {

            long ct = System.currentTimeMillis(); //get current time
            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, LocationTracker.class);
            PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
            mgr.set(AlarmManager.RTC_WAKEUP, ct + LocationTracker.trackerSetting.getMinTimeToUpdate(), pi);
        }
}
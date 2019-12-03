package app.com.friendstracker.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Anand on 02-08-2017.
 */

public class PermissionHelper {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public static boolean checkLocationPermission(Activity context) {
        if (!isLocationPermissionGranted(context)) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission. ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    public static  boolean isLocationPermissionGranted(Activity context){
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission. ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }
}

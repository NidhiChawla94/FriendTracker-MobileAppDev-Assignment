package app.com.friendstracker.home;

import android.location.Location;

import app.com.friendstracker.login.ProfileModel;
import app.com.locationtracker.LocationTracker;
import app.com.locationtracker.TrackerSettings;

/**
 * Created by Anand on 02-08-2017.
 */

public interface HomeContract {

    interface Presenter {
        void startTrackingDevice();
        void stopTrackingDevice();
        void onSuccessLocationPermission();
        ProfileModel.Profile getProfile();
    }
    interface View{
        void requestLocationPermission();
        void startTrackerService();
        String getAccountId();
    }

}

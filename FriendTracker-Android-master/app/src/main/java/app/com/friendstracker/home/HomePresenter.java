package app.com.friendstracker.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import app.com.friendstracker.login.ProfileDatabaseReader;
import app.com.friendstracker.login.ProfileModel;
import app.com.locationtracker.LocationTracker;
import app.com.locationtracker.TrackerSettings;

/**
 * Created by Anand on 02-08-2017.
 */


public class HomePresenter implements HomeContract.Presenter {

    private final HomeContract.View mView;
    private static final String TAG = "HomePresenter";
    private final ProfileModel profileModel;
    private LocationTracker tracker;

    public HomePresenter(HomeContract.View view, ProfileDatabaseReader databaseReader){
        mView = view;
        profileModel = new ProfileModel(databaseReader);

    }

    @Override
    public void startTrackingDevice() {
        mView.requestLocationPermission();
    }

    @Override
    public void stopTrackingDevice() {
    }

    @Override
    public void onSuccessLocationPermission() {
        Log.d(TAG, "Location permission granted");
        mView.startTrackerService();
    }

    @Override
    public ProfileModel.Profile getProfile() {
        return profileModel.getAccountData(mView.getAccountId());
    }

}

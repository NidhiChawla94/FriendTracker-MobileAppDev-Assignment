package app.com.locationtracker;

/**
 * Created by Anand on 03-08-2017.
 */

public class TrackerSettings {

    private final static float DEFAULT_MIN_DISTANCE = 10.0f;
    private final static int DEFAULT_MIN_TIME = 5 * 60 * 1000;
    private final static float DEFAULT_MIN_ACCURACY = 100.0f;
    public final static TrackerSettings DEFAULT_SETTINGS = new TrackerSettings();

    private int minTime = -1;
    private float minDistance = -1;
    private float minAccuracyToupdate = -1;

    public float getMinDistanceToUpdate() {
        return minDistance<0?DEFAULT_MIN_DISTANCE:minDistance;
    }

    public TrackerSettings setMinDistanceToUpdate(float minDistance) {
        this.minDistance = minDistance;
        return this;
    }

    public int getMinTimeToUpdate() {
        return minTime<0?DEFAULT_MIN_TIME:minTime;
    }

    public TrackerSettings setMinTimeToUpdate(int minTime) {
        this.minTime = minTime;
        return this;
    }

    public float getMinAccuracyToupdate() {
        return minAccuracyToupdate<0?DEFAULT_MIN_ACCURACY:minAccuracyToupdate;
    }

    public TrackerSettings setMinAccuracyToupdate(float minAccuracyToupdate) {
        this.minAccuracyToupdate = minAccuracyToupdate;
        return this;
    }
}

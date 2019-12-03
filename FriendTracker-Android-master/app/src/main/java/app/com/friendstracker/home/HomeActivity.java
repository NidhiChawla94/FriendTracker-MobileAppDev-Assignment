package app.com.friendstracker.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import app.com.friendstracker.R;
import app.com.friendstracker.login.ProfileDatabaseReader;
import app.com.friendstracker.login.ProfileModel;
import app.com.friendstracker.util.ImagePicker;
import app.com.friendstracker.utilities.PermissionHelper;
import app.com.locationtracker.LocationTracker;
import app.com.locationtracker.TrackerSettings;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeContract.View {

    private static final int START_DELAY = 1;
    private static final long UPDATE_INTERVAL = 5 * 1000;
    private DrawerLayout drawer;
    private HomePresenter mHomepresenter;
    private PendingIntent tracking;
    private AlarmManager alarms;
    private TextView welcomeText;
    private ImageView navBarImage;
    private TextView navBarNameText;
    private TextView navBarPhoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        welcomeText = (TextView)findViewById(R.id.welcome_text);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        navBarNameText = (TextView)header.findViewById(R.id.nav_nameText);
        navBarPhoneText = (TextView)header.findViewById(R.id.nav_phoneText);
        navBarImage = (ImageView)header.findViewById(R.id.nav_photo);
        navigationView.setNavigationItemSelectedListener(this);
        mHomepresenter = new HomePresenter(this, new ProfileDatabaseReader(this));
        alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onTrackButtonClick(View view){
        mHomepresenter.startTrackingDevice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            // Handle the camera action
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadContent(mHomepresenter.getProfile());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(PermissionHelper.isLocationPermissionGranted(this)){
            mHomepresenter.onSuccessLocationPermission();
        }
    }

    @Override
    public void requestLocationPermission() {
        if(PermissionHelper.checkLocationPermission(this)){
            mHomepresenter.onSuccessLocationPermission();
        };
    }

    @Override
    public void startTrackerService() {
        LocationTracker.start(this, new TrackerSettings().setMinDistanceToUpdate(100.0f).setMinTimeToUpdate(5 * 1000).setMinAccuracyToupdate(100.0f));
    }

    @Override
    public String getAccountId() {
        return getIntent().getStringExtra(ProfileModel.PHONE);
    }

    public void loadContent(ProfileModel.Profile profile) {
        welcomeText.setText(getResources().getString(R.string.welcomeText)+" "+ profile.getName()+"!");
        navBarPhoneText.setText("+"+profile.getPhone());
        navBarNameText.setText(profile.getName());
        navBarImage.setImageBitmap(ImagePicker.blobToBitmap(profile.getPhoto()));
    }


}

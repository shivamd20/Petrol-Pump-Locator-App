package com.sstc.shivam.petrolpumplocator;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sstc.shivam.petrolpumplocator.detailsScreen.PetrolPumpDetailsActivity;
import com.sstc.shivam.petrolpumplocator.offline.ListLocationFragFragmentOffline;
import com.sstc.shivam.petrolpumplocator.offline.OfflineFrag;
import com.sstc.shivam.petrolpumplocator.offline.database.PrefSingleton;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem;
import com.sstc.shivam.petrolpumplocator.startScreen.ListLocationFragFragment;
import com.sstc.shivam.petrolpumplocator.startScreen.StartScreenFrag;

public class MainActivity extends AppCompatActivity implements ListLocationFragFragment.OnListFragmentInteractionListener, ListLocationFragFragmentOffline.OnListFragmentOfflineInteractionListener
,OfflineFrag.OnFragmentInteractionListener,StartScreenFrag.OnStartFragIntersectionLisner{

    public static MainActivity mA;
    OfflineFrag aboutFrag;
    ListLocationFragFragmentOffline offlineList;
    ListLocationFragFragment listLocationFragFragment;
    BottomNavigationView navigation;
    Fragment selectedFragment=null;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    StartScreenFrag startScreenFrag;
    Boolean isStartScreen=true;
    Fragment tempStartFrag;
    GPSTracker gps;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:

                        selectedFragment=StartScreenFrag.newInstance("",MainActivity.this);


                    /*
                    if (offlineList == null) {
                        if(listLocationFragFragment==null) {
                            listLocationFragFragment=new ListLocationFragFragment();
                            selectedFragment = listLocationFragFragment;
                        }
                        else {
                            selectedFragment = listLocationFragFragment;
                        }
                    }

                    else {
                        selectedFragment = offlineList;
                    }*/

                    break;

                case R.id.navigation_notifications:

                    selectedFragment = aboutFrag;

                            break;
            }
            replaceFrame(selectedFragment);
            return true;
        }
    };

    boolean checkForOffline() {
        return PrefSingleton.getInstance().getPreference().getBoolean(PrefSingleton.keyOffline, false);
    }

    void replaceFrame(Fragment selectedFragment) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, selectedFragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mA = this;
        PrefSingleton.getInstance().Initialize(getApplicationContext());

        aboutFrag = OfflineFrag.newInstance("", "");

        /*
        if (checkForOffline()) {
            offlineList = new ListLocationFragFragmentOffline();
        } else {
            offlineList = null;
        }

        if (offlineList == null) {
            listLocationFragFragment = ListLocationFragFragment.newInstance(1);

            replaceFrame(listLocationFragFragment);
        } else {
            replaceFrame(offlineList);
        }*/

        startScreenFrag=StartScreenFrag.newInstance("",this);
        tempStartFrag=startScreenFrag;
        replaceFrame(startScreenFrag);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        // Set the adapter for the list view
        // Set the list's click listener
      //  mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }



    @Override
    public void onListFragmentInteraction(PetrolPumpItem item) {

        Intent i2 = new Intent(this, PetrolPumpDetailsActivity.class);
        i2.putExtra(PetrolPumpItem.EXTRASTRING,item);

        startActivity(i2);
        overridePendingTransition( R.anim.zoomin, R.anim.zoomout );
        
    }

    @Override
    public void onListFragmentOfflineInteraction(PetrolPumpItem item) {

        if(item==null)
        {
            Log.e("offline tion","item is null");
        }
        else {
            Log.e("offline tion",item.address);
        }

        Intent i2 = new Intent(this, PetrolPumpDetailsActivity.class);
        i2.putExtra(PetrolPumpItem.EXTRASTRING,item);

        startActivity(i2);
        overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_right );

    }

    @Override
    public void onBackPressed(){
        if((selectedFragment==tempStartFrag))
        {
            selectedFragment=StartScreenFrag.newInstance("",this);
            replaceFrame(selectedFragment);
            return;
        }
        else
        {
        }

        {new AlertDialog.Builder(this)
                .setTitle("Exit?")
                .setMessage("Are you sure you want to exit this app")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        }
    }

    public Location getLocationFromGps()
    {
        gps = new GPSTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(this, "Your Location is - \nLat: "
                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            return gps.getLocation();

        } else {

            gps.showSettingsAlert();
        }
        return null;
    }

    public void onSearchButtonPressed(){

        requestForPermission();

        Location l=getLocationFromGps();
        if(l!=null) {
            if (checkForOffline()) {
                offlineList = ListLocationFragFragmentOffline.newInstance(0, l);
                tempStartFrag = offlineList;
            } else {
                listLocationFragFragment = ListLocationFragFragment.newInstance(1, l);
                tempStartFrag = listLocationFragFragment;
            }
            selectedFragment = tempStartFrag;
            replaceFrame(tempStartFrag);
        }
    };

    public void onMapButtonPressed(){


        /*
        Location l=getLocationFromGps();
        if(l!=null) {
            if (checkForOffline()) {
                offlineList = ListLocationFragFragmentOffline.newInstance(0, l);
                tempStartFrag = offlineList;
            } else {
                listLocationFragFragment = ListLocationFragFragment.newInstance(1, l);
                tempStartFrag = listLocationFragFragment;
            }
            selectedFragment = tempStartFrag;
            replaceFrame(tempStartFrag);
        }*/

        requestForPermission();
        onSearchRequested();
    };

    @Override
    public boolean onSearchRequested() {

        Bundle appData = new Bundle();
        appData.putBoolean(SearchableActivity.OFFLINE, checkForOffline());
        startSearch(null, false, appData, false);
        return true;
    }
    public void onLocationButtonPressed(){

       if( !requestForPermission()){
           return;
       }
        Location l=getLocationFromGps();
        if(l!=null) {
            if (checkForOffline()) {
                offlineList = ListLocationFragFragmentOffline.newInstance(0, l);
                tempStartFrag = offlineList;
            } else {
                listLocationFragFragment = ListLocationFragFragment.newInstance(1, l);
                tempStartFrag = listLocationFragFragment;
            }
            selectedFragment = tempStartFrag;
            replaceFrame(tempStartFrag);
        }
    }

   boolean checkForPermission()
    {

        int permissionCheckFine = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckCourse = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if((permissionCheckFine==android.content.pm.PackageManager.PERMISSION_GRANTED )&&
                (permissionCheckCourse==android.content.pm.PackageManager.PERMISSION_GRANTED))
        {
            return true;
        }
        else
        {
            return false;
        }

    }

   boolean requestForPermission()
    {
        if (!checkForPermission()  ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        5);


                Toast.makeText(this,"check fir permissons asked",Toast.LENGTH_SHORT).show();

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        Toast.makeText(this,"check fir permissons called",Toast.LENGTH_SHORT).show();
        return checkForPermission();
    }

}



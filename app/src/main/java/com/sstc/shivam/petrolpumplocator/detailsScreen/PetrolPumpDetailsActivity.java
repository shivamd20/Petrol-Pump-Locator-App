package com.sstc.shivam.petrolpumplocator.detailsScreen;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.view.GestureDetector;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sstc.shivam.petrolpumplocator.GPSTracker;
import com.sstc.shivam.petrolpumplocator.R;
import com.sstc.shivam.petrolpumplocator.offline.database.Contract;
import com.sstc.shivam.petrolpumplocator.offline.database.DBHelper;
import com.sstc.shivam.petrolpumplocator.petrolPumpDetails.PetrolPumpItem;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.views.overlay.mylocation.SimpleLocationOverlay;

import java.util.ArrayList;

public class PetrolPumpDetailsActivity extends Activity {


    protected GeoPoint startPoint, destinationPoint;



    PetrolPumpDetailsActivity pPDA = this;
    Activity mA;
    MyLocationNewOverlay mLocationOverlay;
    CompassOverlay mCompassOverlay;
    WebView webView;
    PetrolPumpItem item;
    FloatingActionButton fab;
    GPSTracker gps;

    ImageView restroom,water,toilet,shop,card_pay,air,atm,first_aid,petrol,disel;

    TextView pnameView,contactNoView,addressView,openingTimeView,closingTimeView,websiteView,
            emailIdView,descriptionView,pricePetrolView,priceDiselView,visitsView,stateView,lastUpdatedView;

    RatingBar ratingbar;
    MapView map;
    SimpleLocationOverlay mMyLocationOverlay;
    ScaleBarOverlay mScaleBarOverlay;
    ItemizedIconOverlay<OverlayItem> currentLocationOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_scrolling);


        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        item=(PetrolPumpItem) getIntent().getSerializableExtra(PetrolPumpItem.EXTRASTRING);
        if(item==null)
        {
            throw new NullPointerException();
        }


        DBHelper dbHelper=new DBHelper(this);
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put(Contract.USER_HISTORY.PID,item.pid);
        values.put(Contract.USER_HISTORY.ADDRESS,item.address);
        values.put(Contract.USER_HISTORY.PNAME,item.pname);
        db.insert(Contract.USER_HISTORY.TABLE_NAME,null,values);

        final GestureDetector gestureDetector = new GestureDetector(new MyGestureDetector(this.getParent()));
        //the parent layout
       /* findViewById(R.id.coordinattelayoutdetails).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) return false;
                return false;
            }
        });*/

        Context ctx = this.getApplicationContext();
        //important! set your user agent to prevent getting banned from the osm servers
        org.osmdroid.config.Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));



        //setContentView(R.layout.activity_main);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.

        // This contains the MapView in XML and needs to be called after the account manager


        gps=new GPSTracker(this);

        Location location=getLocationFromGps();

        startPoint=new GeoPoint(location.getLatitude(),location.getLongitude());


        RelativeLayoutTouchListener rLis=new RelativeLayoutTouchListener(this);

        destinationPoint=new GeoPoint(Double.parseDouble(item.latitude),Double.parseDouble(item.longitude));


        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(15);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);

       mapController.setCenter(new GeoPoint(Double.parseDouble(item.latitude),Double.parseDouble(item.longitude)));

       // mapController.setCenter(new GeoPoint(21,81));


        this.mCompassOverlay = new CompassOverlay(this,
                new InternalCompassOrientationProvider(this), map);
        this.mCompassOverlay.enableCompass();
        map.getOverlays().add(this.mCompassOverlay);


         mMyLocationOverlay = new SimpleLocationOverlay(this);
        map.getOverlays().add(mMyLocationOverlay);

        mScaleBarOverlay = new ScaleBarOverlay(map);
        map.getOverlays().add(mScaleBarOverlay);


        final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

        /////////////////
        OverlayItem myLocationOverlayItem = new OverlayItem("Here", "Petrol Pump", destinationPoint);
        Drawable myCurrentLocationMarker = this.getResources().getDrawable(R.drawable.destination_point);
        myLocationOverlayItem.setMarker(myCurrentLocationMarker);
        items.add(myLocationOverlayItem);

         myLocationOverlayItem = new OverlayItem("Here", "Current Position", startPoint);


         myCurrentLocationMarker = this.getResources().getDrawable(R.drawable.start);
        myLocationOverlayItem.setMarker(myCurrentLocationMarker);

        items.add(myLocationOverlayItem);

        AddPolygonAsynTask addPolygonAsynTask=new AddPolygonAsynTask();
        addPolygonAsynTask.execute();

        currentLocationOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        return true;
                    }
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return true;
                    }
                }, this);
        map.getOverlays().add(this.currentLocationOverlay);

        getAllFacButton();
        setAllButtons();

        fab=(FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PetrolPumpDetailsActivity.this,""+item.longitude+item.pname,Toast.LENGTH_SHORT).show();
               Uri gmmIntentUri = Uri.parse("google.navigation:q="+item.latitude+",  "+item.longitude+"");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        map.invalidate();

        initializeViews();

    }

    void initializeViews()
    {
        pnameView=(TextView) findViewById(R.id.pname_view);
        addressView=(TextView) findViewById(R.id.address);
        pricePetrolView=(TextView) findViewById(R.id.price_petrol);
        priceDiselView=(TextView) findViewById(R.id.price_diesel);
        openingTimeView=(TextView) findViewById(R.id.opening_time);
        closingTimeView=(TextView) findViewById(R.id.closing_time);
        descriptionView=(TextView) findViewById(R.id.description);
        emailIdView=(TextView) findViewById(R.id.email);
        websiteView=(TextView) findViewById(R.id.website);
        visitsView=(TextView) findViewById(R.id.visits);
        lastUpdatedView=(TextView) findViewById(R.id.last_updated);
        ratingbar=(RatingBar)findViewById(R.id.ratingBar) ;

        pnameView.setText(item.pname);
        addressView.setText(item.address);
        pricePetrolView.setText(item.price_petrol);
        priceDiselView.setText(item.price_diesel);
        openingTimeView.setText(item.opening_time);
        closingTimeView.setText(item.closing_time);
        descriptionView.setText(item.description);
        emailIdView.setText(item.email_id);
        websiteView.setText(item.website);
        lastUpdatedView.setText(item.last_updated);
        visitsView.setText("visits:  "+item.visits);

        if(item.rating!=null)
        ratingbar.setRating(Integer.parseInt(item.rating));

    }

    @Override
    protected void onResume() {
        super.onResume();

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    @Override
    public void onDetachedFromWindow() {
        map.onDetach();
        super.onDetachedFromWindow();
    }

    void getAllFacButton()
    {
        restroom=(ImageView)findViewById(R.id.rest_room);
        water=(ImageView)findViewById(R.id.water);
        toilet=(ImageView)findViewById(R.id.toilet);
        shop=(ImageView)findViewById(R.id.shop);
        card_pay=(ImageView)findViewById(R.id.card_pay);
        air=(ImageView)findViewById(R.id.air);
        atm=(ImageView)findViewById(R.id.atm);
        first_aid=(ImageView)findViewById(R.id.first_aid);
        petrol=(ImageView)findViewById(R.id.petrol);
        disel=(ImageView)findViewById(R.id.diesel);
    }

    void setAllButtons()
    {
        LinearLayout linearLayout=(LinearLayout) findViewById(R.id.linear_layout_facilities_2);
        if(item.rest_room==null)
        {
            restroom.setEnabled(false);
            linearLayout.removeView(restroom);
        }
        if(item.water==null)
        {
            water.setEnabled(false);
            linearLayout.removeView(water);
        }
        if(item.toilets==null)
        {
            toilet.setEnabled(false);

            linearLayout.removeView(toilet);
        }
        if(true)
        {
            shop.setEnabled(false);

            linearLayout.removeView(shop);
        }
        if(item.card_accepted==null)
        {
            card_pay.setEnabled(false);

            linearLayout.removeView(card_pay);
        }

        if(item.air==null)
        {
            air.setEnabled(false);

            linearLayout.removeView(air);
        }
        if (item.atm == null || (item.atm.compareTo("0") == 0))
        {
            atm.setEnabled(false);

            linearLayout.removeView(atm);
        }
        if(item.first_aid==null)
        {
            first_aid.setEnabled(false);

            linearLayout.removeView(first_aid);
        }
        if(item.petrol==null)
        {
            petrol.setEnabled(false);

            linearLayout.removeView(petrol);
        }
        if(item.disel==null)
        {
            disel.setEnabled(false);

            linearLayout.removeView(disel);
        }
    }


    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }
    public Location getLocationFromGps()
    {
        gps = new GPSTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line

            return gps.getLocation();

        } else {

            gps.showSettingsAlert();
        }
        return null;
    }



    class AddPolygonAsynTask extends AsyncTask<Object,Integer,Polyline>
    {

        Marker nodeMarker;

        @Override
        protected Polyline doInBackground(Object... params) {

            RoadManager roadManager = new OSRMRoadManager(PetrolPumpDetailsActivity.this);

            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();

            waypoints.add(startPoint);

            waypoints.add(destinationPoint);

            Road road = roadManager.getRoad(waypoints);

            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);



            return roadOverlay;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Polyline polyline) {

            /*
            Drawable nodeIcon = getResources().getDrawable(R.drawable.bonuspack_bubble);

            Marker marker=new Marker(map);

            marker.setPosition(startPoint);

            marker.setIcon(nodeIcon);

            marker.setTitle("start");

            map.getOverlays().add(nodeMarker);
*/

           map.getOverlays().add(polyline);
            map.invalidate();
            super.onPostExecute(polyline);
        }
    }
}


package com.firebase.sfvehicles;

import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SFVehiclesActivity extends FragmentActivity implements GeoQueryEventListener, GoogleMap.OnCameraChangeListener,LocationListener {

    private static final GeoLocation INITIAL_CENTER = new GeoLocation(37.7789, -122.4056973);
   // private static final GeoLocation INITIAL_CENTER = new GeoLocation(37.7789, -122.4017);
    private static final int INITIAL_ZOOM_LEVEL = 14;
   // private static final String GEO_FIRE_DB = "https://publicdata-transit.firebaseio.com";
    private static final String GEO_FIRE_DB = "https://chatapppoc-b9a57.firebaseio.com/";
    //private static final String GEO_FIRE_REF = GEO_FIRE_DB + "/_geofire";
//    private static final String GEO_FIRE_REF = GEO_FIRE_DB + "/geolocation";
    private static final String GEO_FIRE_REF = GEO_FIRE_DB + "/users";


    private GoogleMap map;
    private Circle searchCircle;
    private GeoFire geoFire;
    private GeoQuery geoQuery;

    private Map<String,Marker> markers;

    //Current Locaiton
    Location currentLocation ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfvehicles);


        // setup map and camera position
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        this.map = mapFragment.getMap();
        LatLng latLngCenter = new LatLng(INITIAL_CENTER.latitude, INITIAL_CENTER.longitude);
        this.searchCircle = this.map.addCircle(new CircleOptions().center(latLngCenter).radius(2000));
        this.searchCircle.setFillColor(Color.argb(66, 255, 100, 0));
        this.searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));
        this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, INITIAL_ZOOM_LEVEL));
        this.map.setOnCameraChangeListener(this);

        FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId("geofire").setDatabaseUrl(GEO_FIRE_DB).build();
        FirebaseApp app = FirebaseApp.initializeApp(this, options);

        System.out.println("app"+app);
        // setup GeoFire
        this.geoFire = new GeoFire(FirebaseDatabase.getInstance(app).getReferenceFromUrl(GEO_FIRE_REF));
        System.out.println(this.geoFire);
     // geoFire.setLocation("Suneha", new GeoLocation(37.7854880, -122.4056973));
       // geoFire.setLocation("shruti tirpude", new GeoLocation(37.7853889, -122.4056973));
        geoFire.setLocation("pooja", new GeoLocation(37.778, -122.4060073));
        // radius in km

        this.geoQuery = this.geoFire.queryAtLocation(INITIAL_CENTER, 50);
        System.out.println("geoQuery.toString()"+geoQuery.toString());

        // creates a new query around [37.7832, -122.4056] with a radius of 0.6 kilometers
      //  GeoQuery geoQuery1
        //this.geoQuery = geoFire.queryAtLocation(new GeoLocation(37.7832, -122.4056), 2);

        // setup markers
        this.markers = new HashMap<String, Marker>();
    }



    @Override
    protected void onStop() {
        super.onStop();
        // remove all event listeners to stop updating in the background
        this.geoQuery.removeAllListeners();
        for (Marker marker: this.markers.values()) {
            marker.remove();
        }
        this.markers.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // add an event listener to start updating locations again
        this.geoQuery.addGeoQueryEventListener(this);
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {

        System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));
        System.out.println("Amazing");
        // Add a new marker to the map
        Marker marker = this.map.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)).title("Bawaaaa::"+key));

        this.markers.put(key, marker);

        System.out.println("Amazingkya to");




    }

    @Override
    public void onKeyExited(String key) {
        System.out.println("oops");
        // Remove any old marker
        Marker marker = this.markers.get(key);
        if (marker != null) {
            marker.remove();
            this.markers.remove(key);
        }
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        // Move the marker
        Marker marker = this.markers.get(key);
        if (marker != null) {
            this.animateMarkerTo(marker, location.latitude, location.longitude);
        }
    }

    @Override
    public void onGeoQueryReady() {
    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Animation handler for old APIs without animation support
    private void animateMarkerTo(final Marker marker, final double lat, final double lng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long DURATION_MS = 3000;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final LatLng startPosition = marker.getPosition();
        handler.post(new Runnable() {
            @Override
            public void run() {
                float elapsed = SystemClock.uptimeMillis() - start;
                float t = elapsed/DURATION_MS;
                float v = interpolator.getInterpolation(t);

                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                marker.setPosition(new LatLng(currentLat, currentLng));

                // if animation is not finished yet, repeat
                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }



    private double zoomLevelToRadius(double zoomLevel) {
        // Approximation to fit circle into view
        return 16384000/Math.pow(2, zoomLevel);
    }

     //@Override public void onLocationChange


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        // Update the search criteria for this geoQuery and the circle on the map
        LatLng center = cameraPosition.target;
        double radius = zoomLevelToRadius(cameraPosition.zoom);
        this.searchCircle.setCenter(center);
        this.searchCircle.setRadius(radius);
        this.geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
        // radius in km
        this.geoQuery.setRadius(radius/1000);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        System.out.println("location"+location);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

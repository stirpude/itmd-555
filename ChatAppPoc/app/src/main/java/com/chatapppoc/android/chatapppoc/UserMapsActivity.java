package com.chatapppoc.android.chatapppoc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.chatapppoc.android.chatapppoc.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.AlertDialog;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Toast;

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

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class UserMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    //GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    private GeoFire geoFire;

    //private Circle searchCircle;
    //private GeoFire geoFire;
    //private GeoQuery geoQuery;

    private Map<String,Marker> markers;

    private static final String GEO_FIRE_DB = "https://chatapppoc-b9a57.firebaseio.com/";
    private static final String GEO_FIRE_REF = GEO_FIRE_DB + "/geolocation";


   // private static final GeoLocation INITIAL_CENTER = new GeoLocation(37.7789, -122.4056973);
    // private static final GeoLocation INITIAL_CENTER = new GeoLocation(37.7789, -122.4017);
    //private static final int INITIAL_ZOOM_LEVEL = 14;
    // private static final String GEO_FIRE_DB = "https://publicdata-transit.firebaseio.com";
    //private static final String GEO_FIRE_DB = "https://chatapppoc-b9a57.firebaseio.com/";
    //private static final String GEO_FIRE_REF = GEO_FIRE_DB + "/_geofire";
//    private static final String GEO_FIRE_REF = GEO_FIRE_DB + "/geolocation";
    //private static final String GEO_FIRE_REF = GEO_FIRE_DB + "/users";

    //FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId("geofire").setDatabaseUrl(GEO_FIRE_DB).build();
    //FirebaseApp app = FirebaseApp.initializeApp(this, options);

   // this.geoFire = new GeoFire(FirebaseDatabase.getInstance(app).getReferenceFromUrl(GEO_FIRE_REF));
        //System.out.println(this.geoFire);
      // geoFire.setLocation("Suneha", new GeoLocation(37.7853880, -122.4056973));



       // this.geoQuery = this.geoFire.queryAtLocation(INITIAL_CENTER, 50);
      //  System.out.println("geoQuery.toString()"+geoQuery.toString());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //LatLng chicago = new LatLng(41, -87);

        //mMap.addMarker(new MarkerOptions().position(chicago).title("I am at home"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(chicago));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
           // Marker marker = this.mMap.addMarker(new MarkerOptions().position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude())).title("Bawaaaa::"));

            FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId("geofire").setDatabaseUrl(GEO_FIRE_DB).build();
            FirebaseApp app = FirebaseApp.initializeApp(this, options);
            this.geoFire = new GeoFire(FirebaseDatabase.getInstance(app).getReferenceFromUrl(GEO_FIRE_REF));

            geoFire.setLocation("suneha", new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()));


            return;
        }

        buildGoogleApiClient();
        //mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient(){

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();



    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


        //String userId = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
       // DatabaseReference ref = FirebaseDatabase.getInstance().getReference("geolocation");

        //GeoFire geofire = new GeoFire(ref);
        //geofire.setLocation(userId,new GeoLocation(location.getLatitude(),location.getLongitude()));


        FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId("geofire").setDatabaseUrl(GEO_FIRE_DB).build();
        FirebaseApp app = FirebaseApp.initializeApp(this, options);





    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}

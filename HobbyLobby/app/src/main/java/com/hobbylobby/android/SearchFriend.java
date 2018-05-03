package com.hobbylobby.android;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hobbylobby.android.chatapppoc.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Authors: Suneha Sanjiv Patil, Shruti Tirpude, Pooja Gupta
 * Date: 04/28/18
 * Final Project
 */

/**
 * Class which handles the event on the search people by skill page
 */
public class SearchFriend extends FragmentActivity implements OnMapReadyCallback {

    // variables declared
    ListView friendList;
    Activity activity;
    String name;
    Firebase reference;
    List<String> users;
    List<String> friendsList;
    List<String> requestList;
    TextView searchSkillText;
    ImageButton searchBtn;
    String searchTxt;
    GeoLocation CURRENT_LOCATION;
    private DatabaseReference database;
    private GeoFire geofire;
    Boolean usersFetchedByDistance = false;
    private List<String> peopleNearby = new ArrayList<>();
    private Map<String, Marker> markers;
    private GoogleMap map;

    private static final int INITIAL_ZOOM_LEVEL = 14;
    GeoLocation loc;
    TextView noSearchFoundTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        // set logo

        // variables defined
        activity = this;
        friendList = (ListView) findViewById(R.id.friendList);
        searchSkillText = (TextView) findViewById(R.id.searchTxt);
        searchBtn = (ImageButton) findViewById(R.id.searchBtn);
        reference = new Firebase(getString(R.string.firebase_database));
        CURRENT_LOCATION = UserDetails.getUserLocation();
        noSearchFoundTxt = (TextView) findViewById(R.id.noSearchResultTxt);

        try {
            setUpMap();

            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchTxt = searchSkillText.getText().toString();
                    noSearchFoundTxt.setText("");

                    map.clear();
                    if (!searchTxt.isEmpty()) {
                        searchPeopleWithSkill();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Method to get all the users within certain miles
     */
    private void fetchUsersByMiles() {
        GeoQuery geoQuery = geofire.queryAtLocation(CURRENT_LOCATION, 0.4);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Location to = new Location("to");
                to.setLatitude(location.latitude);
                to.setLongitude(location.longitude);
                if (!usersFetchedByDistance) {
                    peopleNearby.add(key);
                }
            }

            @Override
            public void onKeyExited(String key) {
                Log.d("Map", "onKeyExited: ");

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.d("Map", "onKeyMoved: ");
            }

            @Override
            public void onGeoQueryReady() {
                Log.d("Map", "onGeoQueryReady: ");
                usersFetchedByDistance = true;
            }


            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e("Map", "onGeoQueryError: ", error.toException());
            }
        });

    }

    /**
     * Method to get all the people with certain skill and within certain miles
     */
    private void searchPeopleWithSkill() {
        getFriendsInRequestList();
    }

    /**
     * Method to get all the people which are in the logged in user's request list
     */
    private void getFriendsInRequestList() {
        requestList = new ArrayList<>();
        reference.child(getString(R.string.users)).child(UserDetails.username).child(getString(R.string.request_list)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Map<String, Boolean> req = (Map<String, Boolean>) dataSnapshot.getValue();
                    for (Map.Entry<String, Boolean> entry : req.entrySet()) {
                        if (!entry.getValue()) {
                            requestList.add(entry.getKey());
                        }
                    }
                }
                getFriendsList();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * Method to get all the people in the logged in user's friend list
     */
    private void getFriendsList() {
        friendsList = new ArrayList<>();

        reference.child(getString(R.string.users)).child(UserDetails.username).child(getString(R.string.friends_list)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Boolean> map = (Map<String, Boolean>) dataSnapshot.getValue();
                if (dataSnapshot.getValue() != null) {
                    for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                        friendsList.add(entry.getKey());
                    }
                }
                getPeopleWithSkill();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * Method to get all the users with a certain skill
     */
    private void getPeopleWithSkill() {
        users = new ArrayList<>();
        reference.child(getString(R.string.users)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        if (!ds.getKey().equals(UserDetails.username) && !friendsList.contains(ds.getKey()) && !requestList.contains(ds.getKey())
                                && peopleNearby.contains(ds.getKey())) {
                            name = ds.getKey();
                            users.add(name);
                        }
                    } catch (Exception e) {
                        Log.i("Error", e.getMessage());
                    }

                }

                try {
                    filterUserHavingSkill(searchTxt);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    /**
     * Method to filter people with certain skill
     *
     * @param skill the skill on which people need to be filtered
     * @throws InterruptedException
     */
    private void filterUserHavingSkill(final String skill) throws InterruptedException {
        final List<String> filteredUser = new ArrayList<>();
        final AddFriendList[] addFriendCustomAdapter = new AddFriendList[1];
        if (users.size() == 0) {
            noSearchFoundTxt.setText(R.string.no_friends_found);
        }
        for (final String user : users) {
            reference.child(getString(R.string.users)).child(user).child(getString(R.string.skills)).orderByChild(getString(R.string.skill_name)).equalTo(skill.toUpperCase()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        filteredUser.add(user);
                        addMarker(name);
                    }

                    addFriendCustomAdapter[0] = new AddFriendList(activity, filteredUser, reference, getString(R.string.users), getString(R.string.request_list));
                    friendList.setAdapter(addFriendCustomAdapter[0]);
                    if (filteredUser.size() == 0) {
                        noSearchFoundTxt.setText(R.string.no_friends_found);
                    }

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }


    }

    /**
     * Method to add map to the page
     */
    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.markers = new HashMap<String, Marker>();
    }

    /**
     * Method to set firebase and geofire reference
     */
    private void setupFirebase() {
        database = FirebaseDatabase.getInstance().getReference();
        geofire = new GeoFire(database.child(getString(R.string.geolocation)));
    }

    /**
     * Add marker to the map
     *
     * @param key user name whose location is to be marked onto map
     */
    private void addMarker(String key) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.geolocation));
        GeoFire geofire = new GeoFire(reference);
        geofire.getLocation(key, new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                loc = location;
                Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(loc.latitude, loc.longitude)).title(name));
                markers.put(name, marker);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLngCenter = new LatLng(CURRENT_LOCATION.latitude, CURRENT_LOCATION.longitude);
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngCenter, INITIAL_ZOOM_LEVEL));

        setupFirebase();

        fetchUsersByMiles();
    }


}

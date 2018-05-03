package com.hobbylobby.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.hobbylobby.android.chatapppoc.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Authors: Suneha Sanjiv Patil, Shruti Tirpude, Pooja Gupta
 * Date: 04/28/18
 * Final Project
 */

/**
 * Class which handles friend requests coming for the user and displays the list of the requests came for the user
 */
public class Requests extends AppCompatActivity {

    // variables
    ListView requestsList;
    Firebase reference;
    TextView requestPageTxt;
    AcceptFriendRequestCustomList customListAdapter;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        // set logo
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // get reference of the views
        requestsList = (ListView) findViewById(R.id.friendRequestList);
        requestPageTxt = (TextView) findViewById(R.id.norequestsTxt);
        reference = new Firebase(getString(R.string.firebase_database));
        activity = this;
        getAllRequests();


    }

    /**
     * Method to get all pending requests
     */
    private void getAllRequests() {

        final List<String> requests = new ArrayList<>();
        reference.child(getString(R.string.users)).child(UserDetails.username).child(getString(R.string.request_list)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Thread.sleep(2000);
                if (dataSnapshot.getValue() != null) {
                    Map<String, Boolean> req = (Map<String, Boolean>) dataSnapshot.getValue();
                    for (Map.Entry<String, Boolean> entry : req.entrySet()) {
                        if (!entry.getValue()) {
                            requests.add(entry.getKey());
                        }
                    }
                    customListAdapter = new AcceptFriendRequestCustomList(activity, requests, reference,
                            getString(R.string.users), getString(R.string.request_list), getString(R.string.friends_list));
                    requestsList.setAdapter(customListAdapter);
                }
                if (requests.size() == 0) {
                    requestPageTxt.setText("No Pending Requests");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}

package com.hobbylobby.android;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hobbylobby.android.chatapppoc.R;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Authors: Suneha Sanjiv Patil, Shruti Tirpude, Pooja Gupta
 * Date: 04/28/18
 * Final Project
 */

/**
 * Class which displays and handles events on the Profile page of the user.
 */
public class Profile extends AppCompatActivity {

    // variables
    Firebase reference;
    Button addSkill;
    TextView skill;
    TextView skillList;
    TextView name;
    String skills = "";
    Map<String, String> map;
    Button searchFriendBtn;
    Button showRequestBtn;
    Button showFriendsBtn;
    Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // set logo
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // get reference of the views
        addSkill = (Button) findViewById(R.id.addSkillbtn);
        skill = (TextView) findViewById(R.id.addSkillText);
        skillList = (TextView) findViewById(R.id.skillsList);
        name = (TextView) findViewById(R.id.nametxt);
        searchFriendBtn = (Button) findViewById(R.id.searchFriendBtn);
        showRequestBtn = (Button) findViewById(R.id.showReqBtn);
        showFriendsBtn = (Button) findViewById(R.id.showFriendsBtn);
        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        final Activity activity = this;

        try {
            // get reference of the firebase database
            reference = new Firebase(getString(R.string.firebase_database) + "/" + getString(R.string.users) + "/"
                    + UserDetails.username + "/skills");
            // set username on profile
            name.setText(UserDetails.username.toUpperCase());

            // add skill on click of button if skill not already present
            addSkill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (skill != null && !skill.getText().toString().equals("")) {
                        map = new HashMap<String, String>();
                        // add skill with its proficiency
                        map.put(getString(R.string.skill_name), skill.getText().toString().toUpperCase());
                        map.put(getString(R.string.skill_proficiency), "1");

                        // check if the skill already added
                        Query query = reference.orderByChild(getString(R.string.skill_name)).equalTo(skill.getText().toString());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(getApplicationContext(), "Skill exists already", Toast.LENGTH_SHORT).show();

                                } else // add skill if not added
                                    reference.push().setValue(map);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    } else
                        Toast.makeText(getApplicationContext(), "Please enter a skill", Toast.LENGTH_SHORT).show();
                }
            });


            // get all the skills from the database and display on the profile
            reference.orderByChild(getString(R.string.skill_name)).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.exists()) {
                        Map map = dataSnapshot.getValue(Map.class);
                        skills += map.get(getString(R.string.skill_name)).toString() + ",";
                        skillList.setText(skills);
                        skill.setText("");
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }

            });

            searchFriendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SearchFriend.class);
                    startActivity(intent);
                }
            });

            showRequestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Requests.class);
                    startActivity(intent);
                }
            });

            showFriendsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Friends.class);
                    startActivity(intent);
                }
            });

            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Profile.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }


}

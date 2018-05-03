package com.hobbylobby.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hobbylobby.android.chatapppoc.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
/**
 * Authors: Suneha Sanjiv Patil, Shruti Tirpude, Pooja Gupta
 * Date: 04/28/18
 * Final Project
 */

/**
 * Class which handles binding and actions on Friends custom list
 */
public class Friends extends AppCompatActivity {
    // variables declared
    ListView usersList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    Firebase reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // set logo
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // variables defined
        usersList = (ListView) findViewById(R.id.usersList);
        noUsersText = (TextView) findViewById(R.id.noUsersText);
        pd = new ProgressDialog(Friends.this);
        pd.setMessage("Loading...");
        pd.show();

        // get firebase reference
        reference = new Firebase(getString(R.string.firebase_database) + "/" + getString(R.string.users) + "/" + UserDetails.username + "/" + getString(R.string.friends_list));

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Map<String, Boolean> map = (Map<String, Boolean>) dataSnapshot.getValue();
                    if (dataSnapshot.getValue() != null) {
                        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                            al.add(entry.getKey());
                            totalUsers++;
                        }
                    }

                    if (totalUsers < 1) {
                        noUsersText.setVisibility(View.VISIBLE);
                        usersList.setVisibility(View.GONE);
                    } else {
                        noUsersText.setVisibility(View.GONE);
                        usersList.setVisibility(View.VISIBLE);
                        usersList.setAdapter(new ArrayAdapter<String>(Friends.this, android.R.layout.simple_list_item_1, al));
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // open chat windows on click of a friend in the friend list
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                startActivity(new Intent(Friends.this, Chat.class));
            }
        });

        pd.dismiss();
    }
}


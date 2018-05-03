package com.hobbylobby.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hobbylobby.android.chatapppoc.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Authors: Suneha Sanjiv Patil, Shruti Tirpude, Pooja Gupta
 * Date: 04/28/18
 * Final Project
 */

/**
 * Custom list adapter which displays searched people with an Add button to send friend request to the person
 */
public class AddFriendList extends ArrayAdapter {

    // variables declared
    List<String> users;
    Activity context;
    Firebase reference;
    AlertDialog.Builder builder;
    String usersDBKey;
    String requestListDBKey;

    /**
     * Constructor
     *
     * @param context The current context.
     * @param users   The objects to represent in the ListView
     */
    public AddFriendList(Activity context, List<String> users, Firebase reference, String usersDBKey,
                         String requestListDBKey) {
        super(context, R.layout.activity_add_friend_list, users);

        this.users = users;
        this.context = context;
        this.reference = reference;
        builder = new AlertDialog.Builder(context);
        this.usersDBKey = usersDBKey;
        this.requestListDBKey = requestListDBKey;
    }

    /**
     * Method to add person in the request list of both the accepter of friend request
     * @param requestFrom Person from whom request os coming from
     * @param requestTo Person who is accepting the friend request
     */
    private void addFriend(final String requestFrom, final String requestTo) {
        reference.child(usersDBKey).child(requestTo).child(requestListDBKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // store the values in map structure
                Map<String, Boolean> requests = (Map<String, Boolean>) dataSnapshot.getValue();
                if (requests == null) {
                    Map<String, Boolean> requestName = new HashMap<String, Boolean>();
                    requestName.put(requestFrom, false);
                    reference.child(usersDBKey).child(requestTo).child(requestListDBKey).setValue(requestName);
                } else {
                    requests.put(requestFrom, false);
                    reference.child(usersDBKey).child(requestTo).child(requestListDBKey).setValue(requests);
                }

                // show alert
                AlertDialog alert = builder.setMessage("Friend Request Sent to " + requestTo).create();
                alert.show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

  @Override
    public View getView(final int position, View view, ViewGroup parent) {
        // Inflating a view with list of friend requests
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_add_friend_list, null, true);

        // getting reference of the views
        TextView nameTxt = (TextView) rowView.findViewById(R.id.nametxtView);
        Button addBtn = (Button) rowView.findViewById(R.id.addbtn);

        nameTxt.setText(users.get(position));
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend(UserDetails.username, users.get(position));
                // remove the user from the list when add request is sent and reload the list
                users.remove(users.get(position));
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}

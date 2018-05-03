package com.hobbylobby.android;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
 * Custom list adapter which displays Friends requests with Accept and Request button
 */
public class AcceptFriendRequestCustomList extends ArrayAdapter {

    // variables
    Activity context;
    List<String> users;
    Firebase reference;
    String usersDBKey;
    String requestListDBKey;
    String friendsListDBKey;

    /**
     * Constructor
     *
     * @param context   The current context.
     * @param users     The objects to represent in the ListView.
     * @param reference Firebase database reference
     */
    public AcceptFriendRequestCustomList(Activity context, List<String> users, Firebase reference, String usersDBKey,
                                         String requestListDBKey, String friendsListDBKey) {
        super(context, R.layout.activity_accept_friend_request_custom_list, users);
        this.context = context;
        this.users = users;
        this.reference = reference;
        this.usersDBKey = usersDBKey;
        this.requestListDBKey = requestListDBKey;
        this.friendsListDBKey = friendsListDBKey;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Inflate view with the list of the user's request along with an Accept and Reject request
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_accept_friend_request_custom_list, null, true);
        TextView requestFrom = (TextView) rowView.findViewById(R.id.requestFromtext);
        Button acceptBtn = (Button) rowView.findViewById(R.id.acceptBtn);
        Button rejectBtn = (Button) rowView.findViewById(R.id.rejectBtn);

        final String requesterName = users.get(position);
        requestFrom.setText(requesterName);

        // Action to add a person in the friends list
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add into friend list of the person getting the request
                acceptRequest(UserDetails.username, requesterName);
                // add into friend list of the person who sent the request
                acceptRequest(requesterName, UserDetails.username);

                deleteRequest(requesterName);
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRequest(requesterName);
            }
        });
        return rowView;
    }

    /**
     * deletes entry from the list
     *
     * @param requesterName person name which needs to be deleted
     */
    private void deleteRequest(String requesterName) {
        reference.child(usersDBKey).child(UserDetails.username).child(requestListDBKey).child(requesterName).setValue(true);
        users.remove(requesterName);
        notifyDataSetChanged();
    }

    /**
     * Adds the person in the friendslist
     *
     * @param username      person in whose friendlist another person will be added
     * @param requesterName person who will be added
     */
    private void acceptRequest(final String username, final String requesterName) {
        reference.child(usersDBKey).child(username).child(friendsListDBKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                // store the values in map structure
                Map<String, Boolean> friendList = (Map<String, Boolean>) snapshot.getValue();
                if (friendList == null) {
                    Map<String, Boolean> friendName = new HashMap<String, Boolean>();
                    friendName.put(requesterName, false);
                    reference.child(usersDBKey).child(username).child(friendsListDBKey).setValue(friendName);

                } else {
                    friendList.put(requesterName, false);
                    reference.child(usersDBKey).child(username).child(friendsListDBKey).setValue(friendList);
                }
                // set status in requestList to true
                reference.child(usersDBKey).child(username).child(requestListDBKey).child(requesterName).setValue(true);
            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }
}

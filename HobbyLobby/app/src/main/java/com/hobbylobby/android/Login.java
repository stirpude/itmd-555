package com.hobbylobby.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hobbylobby.android.chatapppoc.R;
import com.firebase.client.Firebase;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Authors: Suneha Sanjiv Patil, Shruti Tirpude, Pooja Gupta
 * Date: 04/28/18
 * Final Project
 */

/**
 * Class which handles login events for the user
 */
public class Login extends AppCompatActivity {
    // variables declared
    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // variables defined
        Firebase.setAndroidContext(this);
        registerUser = (TextView) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        // check if the user is authenticated for the app on click of login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if (user.equals("")) {
                    username.setError(getString(R.string.no_text));
                } else if (pass.equals("")) {
                    password.setError(getString(R.string.no_text));
                } else {
                    String url = getString(R.string.firebase_database) + "/users.json";
                    final ProgressDialog pd = new ProgressDialog(Login.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (s.equals("null")) {
                                Toast.makeText(Login.this, R.string.user_not_found, Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        Toast.makeText(Login.this, getString(R.string.user_not_found), Toast.LENGTH_LONG).show();
                                    } else if (obj.getJSONObject(user).getString("password").equals(pass)) {
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        getUserLocation();
                                        startActivity(new Intent(Login.this, Profile.class));
                                    } else {
                                        Toast.makeText(Login.this, getString(R.string.incorrect_password), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                    rQueue.add(request);
                }

            }
        });
    }

    /**
     * Method to get the users geolocation of the logged in user
     */
    private void getUserLocation() {
        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.geolocation));
            GeoFire geofire = new GeoFire(reference);
            geofire.getLocation(UserDetails.username, new LocationCallback() {
                @Override
                public void onLocationResult(String key, GeoLocation location) {
                    UserDetails.setUserLocation(location);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.error_get_location, Toast.LENGTH_SHORT).show();
        }
    }
}
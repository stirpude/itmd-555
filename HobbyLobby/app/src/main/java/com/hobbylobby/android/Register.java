package com.hobbylobby.android;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Authors: Suneha Sanjiv Patil, Shruti Tirpude, Pooja Gupta
 * Date: 04/28/18
 * Final Project
 */

/**
 * Class which handles registration of a new user and setting geo location of the person registering
 */
public class Register extends AppCompatActivity {
    // variables declared
    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // set logo
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // variables defined
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.registerButton);
        login = (TextView) findViewById(R.id.login);

        try {
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            Firebase.setAndroidContext(this);

            //variables for location

            permissions.add(ACCESS_FINE_LOCATION);
            permissions.add(ACCESS_COARSE_LOCATION);
            Firebase.setAndroidContext(this);

            permissionsToRequest = findUnAskedPermissions(permissions);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


                if (permissionsToRequest.size() > 0)
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }


            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Register.this, Login.class));
                }
            });

            // check the credential conditions and register the user
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user = username.getText().toString();
                    pass = password.getText().toString();

                    if (user.equals("")) {
                        username.setError(getString(R.string.no_text));
                    } else if (pass.equals("")) {
                        password.setError(getString(R.string.no_text));
                    } else if (!user.matches("[A-Za-z0-9]+")) {
                        username.setError(getString(R.string.alpha_numeric));
                    } else if (user.length() < 5) {
                        username.setError(getString(R.string.atleast_five_char));
                    } else if (pass.length() < 5) {
                        password.setError(getString(R.string.atleast_five_char));
                    } else {
                        final ProgressDialog pd = new ProgressDialog(Register.this);
                        pd.setMessage("Loading...");
                        pd.show();

                        String url = getString(R.string.firebase_database) + "/users.json";

                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Firebase reference = new Firebase(getString(R.string.firebase_database) + "/" + getString(R.string.users));

                                // if it is the first user in the database to be registered
                                if (s.equals("null")) {
                                    sharedpreferences.edit().putString("userid", user).commit();
                                    reference.child(user).child(getString(R.string.password)).setValue(pass);
                                    Toast.makeText(Register.this, R.string.registration_success, Toast.LENGTH_LONG).show();
                                    // set geo location of the user registered
                                    setCurrentLocationOfUser();
                                } else {
                                    try {
                                        JSONObject obj = new JSONObject(s);

                                        if (!obj.has(user)) {
                                            sharedpreferences.edit().putString("userid", user).commit();
                                            reference.child(user).child(getString(R.string.password)).setValue(pass);
                                            Toast.makeText(Register.this, R.string.registration_success, Toast.LENGTH_LONG).show();
                                            // set geo location of the user registered
                                            setCurrentLocationOfUser();

                                        } else {
                                            Toast.makeText(Register.this, R.string.user_exists, Toast.LENGTH_LONG).show();
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
                                pd.dismiss();
                            }
                        });

                        RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                        rQueue.add(request);


                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }

    // set geo location in terms of longitude and latitude for the user
    private void setCurrentLocationOfUser() {
        locationTrack = new LocationTrack(Register.this);

        if (locationTrack.canGetLocation()) {

            final double longitude = locationTrack.getLongitude();
            final double latitude = locationTrack.getLatitude();

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String userid = sharedpreferences.getString("userid", "");

            System.out.println("userid" + userid);

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(getString(R.string.geolocation));
            final GeoFire geofire = new GeoFire(reference);
            geofire.setLocation(userid, new GeoLocation(latitude, longitude));

            Toast.makeText(getApplicationContext(), "Location for the user set", Toast.LENGTH_SHORT).show();

        } else {

            locationTrack.showSettingsAlert();
        }
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {

        new AlertDialog.Builder(Register.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }
}

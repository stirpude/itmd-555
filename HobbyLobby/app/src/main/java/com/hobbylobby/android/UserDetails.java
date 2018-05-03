package com.hobbylobby.android;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.firebase.geofire.GeoLocation;
import java.util.Map;
/**
 * Authors: Suneha Sanjiv Patil, Shruti Tirpude, Pooja Gupta
 * Date: 04/28/18
 * Final Project
 */

/**
 * Class which handles the User details of the logged in user.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetails {
    static String username = "";
    static String password = "";
    static String chatWith = "";
    static GeoLocation userLocation;
    public static GeoLocation getUserLocation() {
        return userLocation;
    }

    public static void setUserLocation(GeoLocation userLocation) {
        UserDetails.userLocation = userLocation;
    }


    public static Map<String, String>[] getSkills() {
        return skills;
    }

    public static void setSkills(Map<String, String>[] skills) {
        UserDetails.skills = skills;
    }

    static Map<String,String>[] skills;

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getChatWith() {
        return chatWith;
    }
    public UserDetails(){

    }

    @Override
    public String toString() {
        return "UserDetails{username='" + username + "\', password='" + password + "\', chatWith='" + chatWith + "\'}";
    }

}

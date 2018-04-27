# Android Open Source Intelligent Device - 555 Final Project
 

## Android Final Project HobbyLobby Application Project Description

The application is used to connect people with similar interest ranging from AngularJS, Machine learning,Guitar etc within nearby location.
The application has the chat functionality and used to send messages after connecting with people with similar interest.
The real time current location of the user is saved through GPS tracking.

### Setup

The project is based on Gradle. You can use Android Studio to import
the project. You will also need to register for and add a Google Maps API key.
The Google Play Services and Location needs to be turned on.

#### Adding Google Maps API key

Follow the instructions for [setting up Google Maps for
Android](https://developers.google.com/maps/documentation/android/start#get_an_android_certificate_and_the_google_maps_api_key).
You need to obtain an API key and add it to the [AndroidManifest.xml]


### Firebase

The project loads location data from a set maintained by [Firebase](https://firebase.com).

This sample loads its Firebase and GeoFire dependencies from Maven Central.
If you modify and build GeoFire locally make sure to update to modify the gradle file to load GeoFire
either directly or from your local maven repo.

### GeoFire — Realtime location queries with Firebase

GeoFire is set of open-source libraries for JavaScript, Objective-C, and Java that allow you to store and query a set of keys based on their geographic location. At its heart, GeoFire simply stores locations with string keys. Its main benefit, however, is the possibility of retrieving only those keys within a given geographic area - all in realtime.

GeoFire uses the Firebase database for data storage, allowing query results to be updated in realtime as they change. GeoFire selectively loads only the data near certain locations, keeping your applications light and responsive, even with extremely large datasets. 


### Integrating GeoFire with your data

GeoFire is designed as a lightweight add-on to Firebase. To keep things simple, GeoFire stores data in its own format and its own location within your Firebase database. This allows your existing data format and security rules to remain unchanged while still providing you with an easy solution for geo queries.


## Screenshots of the application


 ![alt text](Images/login.png "Login Screenshot")
 ![alt text](Images/register.png "Register Screenshot" )
 ![alt text](Images/setlocation.png "Setting Location Screenshot")
 ![alt text](Images/skill.png "Adding Skill Screenshot" )
 ![alt text](Images/skilladded.png " After Adding Skill Screenshot" )





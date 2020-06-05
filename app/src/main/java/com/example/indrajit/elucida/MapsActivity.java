package com.example.indrajit.elucida;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;

import com.google.android.gms.location.LocationListener;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    double latitude;
    String reference = "ChIJW5lBXGxXqDsRalsiWUT51dE";
    double longitude;
    private int PROXIMITY_RADIUS = 5000;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    TextView tv;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.style.Fade;
        setContentView(R.layout.activity_maps);
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setSelectedItemId(R.id.action_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.setTitle("Hospitals near you");


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }
        else {
            Log.d("onCreate","Google Play Services available.");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        View fragmentMap;//Code to make map invisible
        fragmentMap = findViewById(R.id.map);
        fragmentMap.setVisibility(View.GONE);

        /*RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setInterpolator(new LinearInterpolator());

        img= findViewById(R.id.imageView2);

        img.startAnimation(rotate);
        rotate.setRepeatCount(Animation.INFINITE);*/

        tv = findViewById(R.id.textView2);
        tv.setVisibility(View.VISIBLE);

        img = findViewById(R.id.imageView4);
        img.setVisibility(View.VISIBLE);

        ImageView view2= findViewById(R.id.imageView2);
        view2.setVisibility(View.INVISIBLE);


    }





    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.clear();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.setMyLocationEnabled(true);

       /* String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", 12f, 2f, "Where the party is at");
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    marker.hideInfoWindow();
                    String markerTitle = marker.getTitle();
                    double dlat = marker.getPosition().latitude;
                    double dlong = marker.getPosition().longitude;
                    final String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", dlat, dlong, markerTitle);
                    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        try {
                            Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(unrestrictedIntent);
                        } catch (ActivityNotFoundException innerEx) {
                            Toast.makeText(MapsActivity.this, "Please install a Google Maps application", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });*/


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                //mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            //mMap.setMyLocationEnabled(true);
        }

        /*Button btnHospital = findViewById(R.id.btnHospital);
        btnHospital.setOnClickListener(new View.OnClickListener() {
            String Hospital = "hospital";
            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                //mMap.clear();
                String url = getUrl(latitude, longitude, Hospital);
                String placeDetails = getPlaceDetails(reference);
                Object[] DataTransfer = new Object[3];
                DataTransfer[0] = mMap;
                DataTransfer[1] = url;
                DataTransfer[2] = placeDetails;
                Log.d("onClick", url);
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(DataTransfer);

                Toast.makeText(MapsActivity.this,"Nearby Hospitals", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    public void show()
    {
        String Hospital = "hospital";
        String url = getUrl(latitude, longitude, Hospital);
        //String placeDetails = getPlaceDetails(reference);  ----For now, not needed
        Object[] DataTransfer = new Object[2];   //Change it to 3 when placeDetails is commented out
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        //DataTransfer[2] = placeDetails;     ----For now, not needed
        Log.d("onClick", url);
        //Log.d("onClick", placeDetails);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
        //Toast.makeText(MapsActivity.this, "Nearby Hospitals", Toast.LENGTH_LONG).show();
    }

    public void showdoctors()
    {
        String Hospital = "doctor";
        String url = getUrl(latitude, longitude, Hospital);
        //String placeDetails = getPlaceDetails(reference);  ----For now, not needed
        Object[] DataTransfer = new Object[2];   //Change it to 3 when placeDetails is commented out
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        //DataTransfer[2] = placeDetails;     ----For now, not needed
        Log.d("onClick", url);
        //Log.d("onClick", placeDetails);
        GetNearbyPlacesDoctors getNearbyPlacesDoctors = new GetNearbyPlacesDoctors();
        getNearbyPlacesDoctors.execute(DataTransfer);
        //Toast.makeText(MapsActivity.this, "Nearby Hospitals", Toast.LENGTH_LONG).show();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }


    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCqWqhkqJBZbPfk9m39DbcjyYiPOqBdnW0");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    private String getPlaceDetails(String reference) {

        StringBuilder googlePlacesDetailsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlacesDetailsUrl.append("&reference=" + reference);
        googlePlacesDetailsUrl.append("&key=" + "AIzaSyCqWqhkqJBZbPfk9m39DbcjyYiPOqBdnW0");

        //Alternative Key = AIzaSyBRaI6vWSTL-W1cJm-SB60xNBjlbb8TMaU - Raeven's key
        Log.d("getUrl", googlePlacesDetailsUrl.toString());
        return (googlePlacesDetailsUrl.toString());
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());

        show();
        showdoctors();

        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


        mCurrLocationMarker= mMap.addMarker(markerOptions);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(13f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

        mMap.moveCamera(cameraUpdate);

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,longitude));


        //img.setVisibility(View.INVISIBLE);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng position = marker.getPosition();
                double latitude = position.latitude;
                double longitude = position.longitude;
                String currentString = marker.getSnippet();
                String[] separated = currentString.split("::");
                String ref = separated[1];

                Log.d("sep", separated[1]);


                StringBuilder Url = new StringBuilder("https://www.google.com/maps/search/?api=1");
                Url.append("&query=" + latitude + "," + longitude);
                Url.append("&query_place_id=" + ref);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Url.toString()));
                startActivity(i);
            }
        });

        tv = findViewById(R.id.textView2);
        tv.setVisibility(View.INVISIBLE);

        img = findViewById(R.id.imageView4);
        img.setVisibility(View.INVISIBLE);

        View fragmentMap = findViewById(R.id.map);
        fragmentMap.setVisibility(View.VISIBLE);

        Toast.makeText(MapsActivity.this,"Current Location", Toast.LENGTH_LONG).show();

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(MapsActivity.this,"Connection Failed", Toast.LENGTH_LONG).show();


    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item){
            switch (item.getItemId()) {
                case R.id.action_contacts:
                    Intent Second = new Intent(MapsActivity.this, Contacts.class);
                    startActivity(Second);
                    break;
                case R.id.action_emergency:
                    Intent Third = new Intent(MapsActivity.this, Emergency.class);
                    startActivity(Third);
                    break;
                case R.id.action_nfc:
                    Intent Fourth= new Intent(MapsActivity.this, nfc.class);
                    startActivity(Fourth);
                    break;

            }

            return true;
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /*public void onDestroy() {

        super.onDestroy();
        Log.d("Yes","Ondestroyactivated");

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Yes", "Ondestroyactivated");
        //Toast.makeText(MapsActivity.this, "Exited from app", Toast.LENGTH_LONG).show();

    }


}
package com.example.indrajit.elucida;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Result;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback {

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;
    private ProgressDialog progress;
    TextView tv,tv2;
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //Hiding the title bar
        getWindow().getAttributes().windowAnimations = R.style.Fade;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        tv = findViewById(R.id.textView4);
        tv.setVisibility(View.INVISIBLE);

        tv2 = findViewById(R.id.textView2);
        tv2.setVisibility(View.INVISIBLE);


    }


    @Override
    public void onConnected(@NonNull Bundle bundle) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Result Result) {
        final Status status = Result.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                tv2 = findViewById(R.id.textView2);
                tv2.setVisibility(View.VISIBLE);


                /*Handler h = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {

                        TextView tv = findViewById(R.id.textView2);
                        tv.setVisibility(View.VISIBLE);
                        Intent i = new Intent().setClass(MainActivity.this, MapsActivity.class);
                        startActivity(i);
                    }
                };*/


                /*tv = findViewById(R.id.textView2);
                tv.setVisibility(View.VISIBLE);

                img = findViewById(R.id.imageView4);
                img.setVisibility(View.VISIBLE);*/


                Handler h = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        Intent i = new Intent().setClass(MainActivity.this, MapsActivity.class);
                        startActivity(i);
                    }
                };

                h.sendEmptyMessageDelayed(0, 10);

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog


                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }


                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {

                /*tv = findViewById(R.id.textView2);
                tv.setVisibility(View.VISIBLE);

                img = findViewById(R.id.imageView4);
                img.setVisibility(View.VISIBLE);*/

                tv2 = findViewById(R.id.textView2);
                tv2.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();

                Handler h = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        Intent i = new Intent().setClass(MainActivity.this, MapsActivity.class);
                        startActivity(i);
                    }
                };

                h.sendEmptyMessageDelayed(0, 10);
            } else {

                TextView tv = findViewById(R.id.textView4);
                tv.setVisibility(View.VISIBLE);

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent().setClass(MainActivity.this, MapsActivity.class);
        startActivity(i);
    }*/




}


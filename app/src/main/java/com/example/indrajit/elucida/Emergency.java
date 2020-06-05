package com.example.indrajit.elucida;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Emergency extends AppCompatActivity {

    public String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.style.Fade;
        setContentView(R.layout.activity_emergency);
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setSelectedItemId(R.id.action_emergency);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.setTitle("Emergency");
        manipulate();
        message = getString(R.string.smsBody)+message+getString(R.string.sms2);
        EditText editText = (EditText) findViewById(R.id.editText5);
        editText.setText(message);
    }

    public void manipulate() {
        try {
            FileInputStream fileIn = openFileInput("patient.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            String s = "";

            char[] inputBuffer = new char[100];

            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            message = " " + s + " ";
            InputRead.close();


        } catch (Exception e) {
            e.printStackTrace();
        }




    }
    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    sendSMS();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void emergency(View view){
        if(isPermissionGranted()){

            sendSMS();

        }
    }

    public void sendSMS() {
        EditText editText = (EditText) findViewById(R.id.editText5);
        message = editText.getText().toString();
        Log.i("Send SMS", "");

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        String phonenum = readbtn();
        smsIntent.putExtra("address", phonenum);
        smsIntent.putExtra("sms_body", message);
        // smsIntent.putExtra("sms_body"  ,new String(Contact1Name));

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
            Toast.makeText(getBaseContext(), "Message sent successfully!",
                    Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "SMS failed! Please try again later.", Toast.LENGTH_SHORT).show();
        }


    }
    public String readbtn() {
        String s = "";

        //reading text from file
        try {
            FileInputStream fileIn = openFileInput("phone.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);

            char[] inputBuffer = new char[100];

            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            InputRead.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item){
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent Second = new Intent(Emergency.this, MapsActivity.class);
                    startActivity(Second);
                    break;
                case R.id.action_contacts:
                    Intent Third = new Intent(Emergency.this, Contacts.class);
                    startActivity(Third);
                    break;
                case R.id.action_nfc:
                    Intent Fourth= new Intent(Emergency.this, nfc.class);
                    startActivity(Fourth);
                    break;
            }

            return true;
        }
    };
}

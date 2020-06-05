package com.example.indrajit.elucida;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Contacts extends AppCompatActivity {

    NotificationCompat.Builder builder;
    String chanID="test";
    String a="",b="",c="",d="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.style.Fade;
        setContentView(R.layout.activity_contacts);
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setSelectedItemId(R.id.action_contacts);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.setTitle("Profile");
        read();
        write();


    }
    public void write()
    {
        EditText ed = (EditText) findViewById(R.id.editText4);

        ed.setText(d);
        EditText ed1 = (EditText) findViewById(R.id.editText3);
        ed1.setText(c);
        EditText ed2 = (EditText) findViewById(R.id.editText2);
        ed2.setText(b);
        EditText ed3 = (EditText) findViewById(R.id.editText);
        ed3.setText(a);

    }
    public void buttonclick(View view)
    {


        EditText txtUserName =  findViewById(R.id.editText);
        String ab =  txtUserName.getText().toString();

        txtUserName =  findViewById(R.id.editText2);
        String cd=  txtUserName.getText().toString();

        txtUserName = findViewById(R.id.editText3);
        String ef = txtUserName.getText().toString();

        txtUserName = findViewById(R.id.editText4);
        String gh =  txtUserName.getText().toString();

        if(TextUtils.isEmpty(ab)||TextUtils.isEmpty(cd)||TextUtils.isEmpty(ef)||TextUtils.isEmpty(gh)) {

            AlertDialog.Builder building = new AlertDialog.Builder(this);
            building.setTitle("Prompt!")
                    .setMessage("Please enter all required information before creating a notification!")
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });

            AlertDialog alert = building.create();
            alert.show();

        }

        else {

            createNotificationChannel();
            createNotification();
        }

    }

    private void createNotification() {
        a="";b="";c="";d="";
        read();

        builder = new NotificationCompat.Builder(this, "test")
                .setSmallIcon(R.drawable.ic_icon)
                .setContentText("Expand for User Details and Emergency Contact")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("User Details:"+"\n"+"Name : "+a+ "\n"+"Blood Group : "+b+ "\n\n"+"Emergency Contact:"+"\n"+"Name : "+c+ "\n"+"Phone number : "+d))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true);

        AlertDialog.Builder building = new AlertDialog.Builder(this);
        building.setTitle("Prompt!")
                .setMessage("This will enable a permanent notification. Do you want to continue?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finalcreate();
                    }

                })
        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }

        });

        AlertDialog alert = building.create();
        alert.show();

        /*NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(123, builder.build());*/

    }

    private void finalcreate()
    {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(123, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void removenoti(View view)
    {

        AlertDialog.Builder building = new AlertDialog.Builder(this);
        building.setTitle("Prompt!")
                .setMessage("This will delete the permanent notification(not advisable). Do you still want to continue?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.deleteNotificationChannel(chanID);
                    }

                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }

                });

        AlertDialog alert = building.create();
        alert.show();
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_des);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("test", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void read() {
        //reading text from file
        try {
            FileInputStream fileIn = openFileInput("phone.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            //String s = "";

            char[] inputBuffer = new char[100];

            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                d += readstring;
            }

            InputRead.close();




        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fileIn = openFileInput("contact.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
           // String s = "";

            char[] inputBuffer = new char[100];

            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                c += readstring;
            }

            InputRead.close();



        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fileIn = openFileInput("blood.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
            //String s = "";

            char[] inputBuffer = new char[100];

            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                b += readstring;
            }

            InputRead.close();




        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            FileInputStream fileIn = openFileInput("patient.txt");
            InputStreamReader InputRead = new InputStreamReader(fileIn);
           // String s = "";

            char[] inputBuffer = new char[100];

            int charRead;

            while ((charRead = InputRead.read(inputBuffer)) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
               a += readstring;
            }

            InputRead.close();




        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void saveData(View view) {
        try {
            FileOutputStream fileout = openFileOutput("phone.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            EditText edit = (EditText) findViewById(R.id.editText4);
            outputWriter.write(edit.getText().toString());
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "Updated successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        emergencyContactName();
        patientName();
        blood();


    }
    public void emergencyContactName() {
        try {
            FileOutputStream fileout = openFileOutput("contact.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            EditText edit = (EditText) findViewById(R.id.editText3);
            outputWriter.write(edit.getText().toString());
            outputWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void patientName() {
        try {
            FileOutputStream fileout = openFileOutput("patient.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            EditText edit = (EditText) findViewById(R.id.editText);
            outputWriter.write(edit.getText().toString());
            outputWriter.close();


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    public void blood() {
        try {
            FileOutputStream fileout = openFileOutput("blood.txt", MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            EditText edit = (EditText) findViewById(R.id.editText2);
            outputWriter.write(edit.getText().toString());
            outputWriter.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }





    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected (@NonNull MenuItem item){
            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent Second = new Intent(Contacts.this, MapsActivity.class);
                    startActivity(Second);
                    break;
                case R.id.action_emergency:
                    Intent Third = new Intent(Contacts.this, Emergency.class);
                    startActivity(Third);
                    break;
                case R.id.action_nfc:
                    Intent Fourth= new Intent(Contacts.this, nfc.class);
                    startActivity(Fourth);
                    break;
            }

            return true;
        }
    };
}

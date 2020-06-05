package com.example.indrajit.elucida;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

public class Internetcheck extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internetcheck);
        getSupportActionBar().hide(); //Hiding the title bar

        isConnected(this);

        TextView tv = findViewById(R.id.textView4);
        tv.setVisibility(View.INVISIBLE);

    }

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            Intent i = new Intent().setClass(Internetcheck.this, MainActivity.class);
            startActivity(i);
            return true;
        } else {
            showDialog();
            return false;
        }
    }

    private void showDialog()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Connection Error")
                .setMessage("Unable to connect to internet. Check your Internet connection and try again.")
                .setCancelable(false)
                /*.setPositiveButton("Connect to Internet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                })*/
                .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        tv = findViewById(R.id.textView4);
                        tv.setVisibility(View.VISIBLE);

                        Handler h = new Handler(){
                            @Override
                            public void handleMessage(Message msg) {
                                tryagain();
                            }
                        };
                        h.sendEmptyMessageDelayed(0, 1000);
                    }
                });
                /*.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        tv = findViewById(R.id.textView4);
                        tv.setVisibility(View.VISIBLE);


                    }
                });*/
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void tryagain() {

        isConnected(this);


    }
}

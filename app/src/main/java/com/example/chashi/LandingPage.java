package com.example.chashi;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;


import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class LandingPage extends AppCompatActivity implements OnOTPSent {

    private AppBarConfiguration mAppBarConfiguration;
    private final int REQ_MSG_READ=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_detect, R.id.nav_profile, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


   //     isPermissionGranted(Manifest.permission.RECEIVE_SMS, REQ_MSG_READ);
      //  isPermissionGranted(Manifest.permission.WRITE_SMS, REQ_MSG_READ);
        new CallOtpSendAPI("8801761002104","5",this).execute();

        // loginParams .put("location", "56.1603092,10.2177147");


    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void setTitleActivity(String text){
        getSupportActionBar().setTitle(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isPermissionGranted(String permission, final int REQ_CODE) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(permission)
                    == PackageManager.PERMISSION_GRANTED) {
                // Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                //  Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{permission}, REQ_CODE);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_MSG_READ:
                //   Log.d(TAG, "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //selectImage();

                } else {
                    //     progress.dismiss();
                }
                break;


        }
    }


    @Override
    public void onTaskCompleted(DOBTransaction dobTransaction) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Title");
        alert.setMessage("Message :");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                Log.d("", "Pin Value : " + value);
                return;
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        return;
                    }
                });
        alert.show();
        //action.getTransId(),Toast.LENGTH_LONG).show();
    }
}

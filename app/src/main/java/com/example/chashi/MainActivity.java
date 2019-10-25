package com.example.chashi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    ImageView imageView_logo;
    FirebaseAuth auth;
    Intent intent,intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        auth = FirebaseAuth.getInstance();
        intent = new Intent(this,Login_activity.class);
        intent1 = new Intent(this,LandingPage.class);
        imageView_logo = findViewById(R.id.imageView);


        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {
                imageView_logo.animate().alpha(1).setDuration(1000);
            }

            @Override
            public void onFinish() {
                new CountDownTimer(1000, 1000) {
                    @Override
                    public void onTick(long l) {
                        imageView_logo.animate().alpha(0).setDuration(1000);
                    }

                    @Override
                    public void onFinish() {
                        if(auth.getCurrentUser() == null) {
                            startActivity(intent1);
                        }
                        else {
                            startActivity(intent1);
                        }

                      // startActivity(intent);

                    }
                }.start();
            }
        }.start();

    }



}

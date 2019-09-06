package com.example.chashi;

import androidx.annotation.NonNull;
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
    LinearLayout linearLayout_login,linearLayout_pin;
    ConstraintLayout constraintLayout_main;
    EditText editText_phone_no,editText_pin1, editText_pin2, editText_pin3, editText_pin4, editText_pin5, editText_pin6;
    String string_phone_no, string_pin,TAG = "log in page",mVerificationId;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    Animation animation_top_to_down;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        editText_pin1= findViewById(R.id.editText2);
        editText_pin2= findViewById(R.id.editText3);
        editText_pin3= findViewById(R.id.editText4);
        editText_pin4= findViewById(R.id.editText5);
        editText_pin5= findViewById(R.id.editText6);
        editText_pin6= findViewById(R.id.editText7);
        editText_phone_no= findViewById(R.id.editText);

        imageView_logo = findViewById(R.id.imageView);
        linearLayout_login = findViewById(R.id.login_side);
        constraintLayout_main = findViewById(R.id.login_layout);
        linearLayout_pin = findViewById(R.id.pin_layout);
        animation_top_to_down = AnimationUtils.loadAnimation(this, R.anim.toptodown);
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                imageView_logo.animate().alpha(1).setDuration(1500);
            }

            @Override
            public void onFinish() {
                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long l) {
                        imageView_logo.animate().alpha(0).setDuration(1500);
                    }

                    @Override
                    public void onFinish() {
                        imageView_logo.setVisibility(View.GONE);
                        constraintLayout_main.setVisibility(View.VISIBLE);

                    }
                }.start();
            }
        }.start();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //verification finished
                Toast.makeText(getApplicationContext(),"Verification Completed!",Toast.LENGTH_LONG).show();
                signInWithPhoneAuthCredential(phoneAuthCredential);
               // startActivity(intent_map);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d(TAG, "onVerificationFailed: "+e.getMessage());
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Toast.makeText(getApplicationContext(),"Invalid Phone number!",Toast.LENGTH_LONG).show();
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Log.d(TAG, "onVerificationFailed: "+e.getMessage());
                    Toast.makeText(getApplicationContext(),"Something went wrong! :(",Toast.LENGTH_LONG).show();
                    // [END_EXCLUDE]
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                    Toast.makeText(getApplicationContext(), "Verification Code(OTP) sent!", Toast.LENGTH_LONG).show();

                //update UI
                // [END_EXCLUDE]
            }

        };
    }
    public void go_without_login(View view){
        // directly go to next intent or activity
        Intent myIntent = new Intent(MainActivity.this, LandingPage.class);
        MainActivity.this.startActivity(myIntent);
    }
    public void login_button(View view){
        String phone_no = editText_phone_no.getText().toString().trim();
        if (!phone_no.isEmpty()) {
            string_phone_no = "+88" + phone_no;
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    string_phone_no,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
            linearLayout_login.setVisibility(View.GONE);
            linearLayout_pin.setVisibility(View.VISIBLE);
            linearLayout_pin.setAnimation(animation_top_to_down);
        }
    }
    public void submit_pin(View view){
        string_pin = editText_pin1.getText().toString().trim()+editText_pin2.getText().toString().trim()+editText_pin3.getText().toString().trim()+editText_pin4.getText().toString().trim()+editText_pin5.getText().toString().trim()+editText_pin6.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, string_pin);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);

    }
    public void resend_pin(View view){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                string_phone_no,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                mResendToken);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //startActivity(intent_sign_up);
                            FirebaseUser user = task.getResult().getUser();
                            // [START_EXCLUDE]
                            // [END_EXCLUDE]
                            databaseReference.child("profile").child(string_phone_no).child("phone").setValue(string_phone_no);
                            Toast.makeText(getApplicationContext(),"Verification Completed!",Toast.LENGTH_LONG).show();
                            //startActivity(intent_map);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                //mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                                Toast.makeText(getApplicationContext(),"Something stopped verification process!",Toast.LENGTH_SHORT).show();
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            //updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
}

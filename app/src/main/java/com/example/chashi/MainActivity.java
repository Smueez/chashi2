package com.example.chashi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView imageView_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView_logo = findViewById(R.id.imageView);
        new CountDownTimer(2000, 1000) {
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

                    }
                }.start();
            }
        }.start();
    }
}

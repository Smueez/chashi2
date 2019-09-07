package com.example.chashi;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class QuesActivity extends AppCompatActivity {
    private TextView quesTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ques);
        quesTitle=findViewById(R.id.questitle);
        String getObj;
        getObj = getIntent().getStringExtra("itemData");
        quesTitle.setText(getObj);
    }
}

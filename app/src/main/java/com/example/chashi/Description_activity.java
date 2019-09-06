package com.example.chashi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Description_activity extends AppCompatActivity {

    String image_url,item_name,disease_name;
    ImageView imageView;
    TextView textView_headings,textView_description;
    DatabaseReference databaseReference;
    Intent intent_go_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_activity);

        item_name = getIntent().getExtras().getString("item_name");
        disease_name = getIntent().getExtras().getString("disease_name");
        image_url = getIntent().getExtras().getString("image_url");

        imageView = findViewById(R.id.disease_pic);
        textView_description = findViewById(R.id.description_text);
        textView_headings = findViewById(R.id.headings);

        textView_headings.setText(item_name+" এর "+ disease_name +"রোগের বিবরণ");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        intent_go_back = new Intent(this,Disease.class);

    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(image_url).getContent());
            imageView.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        databaseReference.child("item").child(item_name).child("disease").child(disease_name).child("description").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView_description.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(intent_go_back);
    }
}

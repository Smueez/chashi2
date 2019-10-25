package com.example.chashi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Description_activity extends AppCompatActivity {

    String image_url,item_name,disease_name;
    ImageView imageView;
    TextView textView_headings,textView_description,blabla;
    DatabaseReference databaseReference;
    Intent intent_go_back;
    ListView listView_buttons;
    List<Disease_item> button_List;
    CardView cardView;

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

        textView_headings.setText(item_name+" এর "+ disease_name +" রোগ");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        intent_go_back = new Intent(this,Disease.class);
        listView_buttons = findViewById(R.id.fertiliser);
        button_List = new ArrayList<>();
        cardView = findViewById(R.id.buttons);
        blabla = findViewById(R.id.blabla);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(image_url!=null){
            Picasso.get().load(image_url).into(imageView);
        }else{
            databaseReference.child("item").child(item_name).child("disease").child(disease_name).child("disease_image_url").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String s=dataSnapshot.getValue(String.class);
                    Picasso.get().load(s).into(imageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



        FirebaseUtilClass.getDatabaseReference().child("item").child(item_name).child("disease").child(disease_name).child("description").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView_description.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        FirebaseUtilClass.getDatabaseReference().child("item").child(item_name).child("disease").child(disease_name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listView_buttons.setAdapter(null);
                button_List.clear();
                if (dataSnapshot.hasChild("fertiliser")){
                    for (DataSnapshot ds : dataSnapshot.child("fertiliser").getChildren()){
                        Disease_item diseaseItem = ds.getValue(Disease_item.class);
                        button_List.add(diseaseItem);
                    }
                    Disease_item_adapter diseaseItemAdapter = new Disease_item_adapter(Description_activity.this,button_List);
                    listView_buttons.setAdapter(diseaseItemAdapter);
                }
                else {
                    cardView.setVisibility(View.GONE);
                    blabla.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        listView_buttons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // item clicked function
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // startActivity(intent_go_back);
    }
}

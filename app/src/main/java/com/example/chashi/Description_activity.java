package com.example.chashi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import android.widget.LinearLayout;
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

    String image_url, item_name, disease_name;
    ImageView imageView;
    TextView textView_headings, textView_description, blabla;
    DatabaseReference databaseReference;
    Intent intent_go_back;



    CardView cardView;



    private RecyclerView inventoryItemRecyclerView;
    private List<Product_item> itemList = new ArrayList<>();
    private InsecticideAdapter mAdapter;

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

        textView_headings.setText(item_name + " এর " + disease_name + " রোগ");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        intent_go_back = new Intent(this, Disease.class);
    //    listView_buttons = findViewById(R.id.fertiliser);
     //   button_List = new ArrayList<>();
        cardView = findViewById(R.id.buttons);
        blabla = findViewById(R.id.blabla);





     //   insecticideAdapter=new InsecticideAdapter(button_List);

     //   LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
     //   mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
     //   listView_buttons.setLayoutManager(mLayoutManager);

    //    listView_buttons.setAdapter(insecticideAdapter);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

initializeRecyclerView();

    }

    private void initializeRecyclerView() {
        inventoryItemRecyclerView = findViewById(R.id.fertiliser);

        mAdapter = new InsecticideAdapter(itemList, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        inventoryItemRecyclerView.setLayoutManager(mLayoutManager);
        inventoryItemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        inventoryItemRecyclerView.setAdapter(mAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (image_url != null) {
            Picasso.get().load(image_url).into(imageView);
        } else {
            FirebaseUtilClass.getDatabaseReference().child("item").child(item_name).child("disease").child(disease_name).child("disease_image_url").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String s = dataSnapshot.getValue(String.class);
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


//01311830024

        FirebaseUtilClass.getDatabaseReference().child("item").child(item_name).child("disease").child(disease_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.hasChild("fertiliser")) {
                    for (DataSnapshot ds : dataSnapshot.child("fertiliser").getChildren()) {
                        String str = ds.getKey();
                        Log.d("gg33-fer",str);
                        FirebaseUtilClass.getDatabaseReference().child("Products").child("বীজ").child("আলু").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Product_item p = dataSnapshot.getValue(Product_item.class);
                                if(p!=null){
                                    Log.d("gg33-pro",p.getName());
                                    itemList.add(p);


                                    mAdapter.notifyDataSetChanged();

                                  //  Log.d("gg33-sz",String.valueOf(insecticideAdapter.getItemCount()));
                                }else {
                                    Log.d("gg33-pro","null");
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //button_List.add(str);
                    }
                  //  Disease_item_adapter diseaseItemAdapter = new Disease_item_adapter(Description_activity.this, button_List);
                    //listView_buttons.setAdapter(diseaseItemAdapter);
                } else {
                    cardView.setVisibility(View.GONE);
                    blabla.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // startActivity(intent_go_back);
    }
}

package com.example.chashi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Disease extends AppCompatActivity {

    ListView recyclerView;
    List<Item_list> item_lists;
    List<Disease_list> disease_lists;
    DatabaseReference databaseReference;
    String TAG = "disease page ", item_name, disease_name, image_url_selected;
    boolean item_tabbed = true;
    TextView textView_highlight;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        textView_highlight = findViewById(R.id.highlight);
        recyclerView = findViewById(R.id.item_recycleView);
        item_lists = new ArrayList<>();
        disease_lists = new ArrayList<>();
        intent = new Intent(this, Description_activity.class);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        item_list_function();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void item_list_function() {
        recyclerView.setAdapter(null);
        item_lists.clear();
        FirebaseUtilClass.getDatabaseReference().child("item").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Item_list itemList = ds.getValue(Item_list.class);
                    item_lists.add(itemList);
                }

                Item_list_adapter item_list_adapter = new Item_list_adapter(Disease.this, item_lists);
                recyclerView.setAdapter(item_list_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    public void disease_list_function(String item_name_selected) {
        recyclerView.setAdapter(null);
        item_lists.clear();
        disease_lists.clear();
        Log.d(TAG, "disease_list_function: " + item_name_selected);
        ;
        FirebaseUtilClass.getDatabaseReference().child("item").child(item_name_selected).child("disease").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Disease_list diseaseList = ds.getValue(Disease_list.class);
                    disease_lists.add(diseaseList);
                }
                Disease_list_adapter disease_list_adapter = new Disease_list_adapter(Disease.this, disease_lists);
                recyclerView.setAdapter(disease_list_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (item_tabbed) {


                    item_name = item_lists.get(i).getName();
                    //image_url_selected= item_lists.get(i).getImage_url();
                    Log.d(TAG, "onItemClick: " + item_name);
                    textView_highlight.setText(item_name + " এর রোগ");
                    disease_list_function(item_name);
                    item_tabbed = false;

                } else {

                    disease_name = disease_lists.get(i).getDisease_name();
                    image_url_selected = disease_lists.get(i).getDisease_image_url();
                    intent.putExtra("disease_name", disease_name);
                    intent.putExtra("item_name", item_name);
                    intent.putExtra("image_url", image_url_selected);
                    startActivity(intent);

                }
            }
        });

    }
/*
    @Override
    public void onBackPressed() {
        if (!item_tabbed) {
            item_tabbed = true;
            Intent intent_back = new Intent(this, Disease.class);
            startActivity(intent_back);
        } else {
            // intent here to go to home activity
            Intent intent1 = new Intent(this, LandingPage.class);
            startActivity(intent1);
        }
    }*/
}

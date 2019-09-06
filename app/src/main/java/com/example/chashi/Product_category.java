package com.example.chashi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Product_category extends AppCompatActivity {

    private RecyclerView categoryRecyclerView;

    private List<Product_item> products = new ArrayList<>();
    private Product_Category_Adapter mAdapter;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        category = getIntent().getExtras().getString("type");

        Toast.makeText(this,category,Toast.LENGTH_SHORT).show();

        initializeRecyclerView();
        readDataFromFirebase();
    }

    private void readDataFromFirebase() {
        FirebaseUtilClass.getDatabaseReference().child("product").child(category).orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    products.add(dsp.getValue(Product_item.class)); //add result into array list
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void initializeRecyclerView() {
        categoryRecyclerView = findViewById(R.id.Category_rclrvw);

        mAdapter = new Product_Category_Adapter(products,this);
        RecyclerView.LayoutManager mLayoutmanager =new LinearLayoutManager(this);
        categoryRecyclerView.setLayoutManager(mLayoutmanager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.setAdapter(mAdapter);
    }
}

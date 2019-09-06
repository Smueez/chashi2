package com.example.chashi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Product_category extends AppCompatActivity {

    private RecyclerView machineRecyclerView;

    private List<Category> categories = new ArrayList<>();
    private Product_Category_Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
    }
}

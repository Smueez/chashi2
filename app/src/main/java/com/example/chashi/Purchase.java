package com.example.chashi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Purchase extends AppCompatActivity {

    private Product_item item;
    private TextView name,price,description;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);


        item = (Product_item) getIntent().getExtras().getSerializable("itemData");

        name = findViewById(R.id.product_name);
        price = findViewById(R.id.product_price);
        description = findViewById(R.id.product_description);
        imageView = findViewById(R.id.product_image);

        name.setText(item.getName());
        price.setText("দামঃ "+item.getPrice());
        description.setText(item.getDesc());
        Picasso.get().load(item.getImage()).into(imageView);


    }
}

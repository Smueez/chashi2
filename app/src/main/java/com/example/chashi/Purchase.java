package com.example.chashi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Purchase extends AppCompatActivity {

    private Product_item item;
    private TextView name,price,description;
    private ImageView imageView;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);


        item = (Product_item) getIntent().getExtras().getSerializable("itemData");

        name = findViewById(R.id.product_name);
        price = findViewById(R.id.product_price);
        description = findViewById(R.id.product_description);
        imageView = findViewById(R.id.product_image);
        editText = findViewById(R.id.amount);
        button = findViewById(R.id.next);

        name.setText(item.getName());
        price.setText("দামঃ "+item.getPrice());
        description.setText(item.getDesc());
        Picasso.get().load(item.getImage()).into(imageView);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryWriteData();
            }
        });

    }

    private void tryWriteData(){

        boolean error =  false;

        String amount = editText.getText().toString();
        if (TextUtils.isEmpty(amount.trim())) {
            editText.setError("Field mustn't be empty");
            editText.requestFocus();
            error = true;
            return;
        }

        if(!error){
            Intent intent = new Intent(this,PurchaseInfo.class);
            intent.putExtra("itemData",item);
            intent.putExtra("amount",amount);
            this.startActivity(intent);
        }
    }
}

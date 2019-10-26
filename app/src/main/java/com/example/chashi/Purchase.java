package com.example.chashi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Purchase extends AppCompatActivity {

    private Product_item item;
    private TextView name,price,description;
    private ImageView imageView;
    private EditText editText;
    private Button button,plus,minus;

    private RecyclerView inventoryItemRecyclerView;
    private String product_id;
    private SubCatagory subCatagory;
    private List<Product_item> itemList = new ArrayList<>();
    private InsecticideAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);


        subCatagory = (SubCatagory) getIntent().getExtras().getSerializable("itemData");
        product_id = getIntent().getExtras().getString("itemId");
        itemList = subCatagory.getProduct_items();

        for(int i = 0;i< itemList.size();i++){
            if(product_id.equals(itemList.get(i).getId())){
                item = itemList.get(i);
            }
        }

        name = findViewById(R.id.product_name);
        price = findViewById(R.id.product_price);
        description = findViewById(R.id.product_description);
        imageView = findViewById(R.id.product_image);
        editText = findViewById(R.id.amount);
        button = findViewById(R.id.next);


        name.setText(item.getName());
        price.setText("দামঃ "+item.getPrice()+" টাকা");
        description.setText(item.getDesc());
        Picasso.get().load(item.getImage()).into(imageView);

        plus = findViewById(R.id.plus_btn);
        minus = findViewById(R.id.minus_btn);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int crntValue;
                if(editText.getText().toString().isEmpty()){
                    crntValue = 0;
                }else{
                    crntValue = Integer.parseInt(editText.getText().toString());
                }

                crntValue++;
                editText.setText(String.valueOf(crntValue));
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int crntValue;
                if(editText.getText().toString().isEmpty()){
                    crntValue = 0;
                }else{
                    crntValue = Integer.parseInt(editText.getText().toString());
                }
                if(crntValue != 0){
                    crntValue--;
                }

                editText.setText(String.valueOf(crntValue));
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryWriteData();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeRecyclerView();


        mAdapter.notifyDataSetChanged();
    }

    private void initializeRecyclerView() {
        inventoryItemRecyclerView = findViewById(R.id.moreProduct);

        mAdapter = new InsecticideAdapter(subCatagory,itemList, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        inventoryItemRecyclerView.setLayoutManager(mLayoutManager);
        inventoryItemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        inventoryItemRecyclerView.setAdapter(mAdapter);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

package com.example.chashi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class PurchaseInfo extends AppCompatActivity {

    private Product_item item;
    private String prod_name,price,des,amount;
    private EditText editTextName,editTextPhnNo,editTextVillage,editTextDistrict,editTextUpozila,editTextDivision;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_info);

        editTextDistrict = findViewById(R.id.district);
        editTextDivision = findViewById(R.id.division);
        editTextName = findViewById(R.id.name);
        editTextVillage = findViewById(R.id.village);
        editTextPhnNo = findViewById(R.id.phone);
        editTextUpozila = findViewById(R.id.upozila);
        confirm = findViewById(R.id.confirm);

        item = (Product_item) getIntent().getExtras().getSerializable("itemData");
        prod_name = item.getName();
        price = item.getPrice();
        des = item.getDesc();
        amount = getIntent().getExtras().getString("amount");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryWriteData();
            }
        });


    }

    private void tryWriteData(){

        boolean error =  false;

        String name = editTextName.getText().toString();
        if (TextUtils.isEmpty(name.trim())) {
            editTextName.setError("Field mustn't be empty");
            editTextName.requestFocus();
            error = true;
            return;
        }

        String phone = editTextPhnNo.getText().toString();
        if (TextUtils.isEmpty(phone.trim())) {
            editTextPhnNo.setError("Field mustn't be empty");
            editTextPhnNo.requestFocus();
            error = true;
            return;
        }

        String village = editTextVillage.getText().toString();
        if (TextUtils.isEmpty(village.trim())) {
            editTextVillage.setError("Field mustn't be empty");
            editTextVillage.requestFocus();
            error = true;
            return;
        }

        String upozila = editTextUpozila.getText().toString();
        if (TextUtils.isEmpty(upozila.trim())) {
            editTextUpozila.setError("Field mustn't be empty");
            editTextUpozila.requestFocus();
            error = true;
            return;
        }

        String district = editTextDistrict.getText().toString();
        if (TextUtils.isEmpty(district.trim())) {
            editTextDistrict.setError("Field mustn't be empty");
            editTextDistrict.requestFocus();
            error = true;
            return;
        }

        String division = editTextDivision.getText().toString();
        if (TextUtils.isEmpty(division.trim())) {
            editTextDivision.setError("Field mustn't be empty");
            editTextDivision.requestFocus();
            error = true;
            return;
        }

        if(!error){

            transaction transaction = new transaction(prod_name,price,des,amount,name,phone,village,upozila,district,division);

            writeDataToFirebase(transaction);

        }
    }

    public void writeDataToFirebase(final transaction item) {
        FirebaseUtilClass.getDatabaseReference().child("Order").push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseInfo.this);
                builder.setMessage("আপনার ক্রয় সম্পন্ন হয়েছে।ধন্যবাদ")
                        .setPositiveButton("ঠিক আছে", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        }).setCancelable(false);
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PurchaseInfo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}

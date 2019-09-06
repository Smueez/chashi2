package com.example.chashi;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AddPostActivity extends AppCompatActivity {
    private final int PICK_IMAGE = 1, REQ_IMG_READ = 2;
    private LinearLayout addImage;
    private EditText quesEditText;
    private ImageView img, crossImg;
    private TextView textView;
    private Uri uri;
    private Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("প্রশ্ন জিজ্ঞাসা করুন");

        addImage=findViewById(R.id.addImageLayout);
        quesEditText=findViewById(R.id.quesEditText);
        img=findViewById(R.id.imgPost);
        textView=findViewById(R.id.addImgMsg);
        crossImg=findViewById(R.id.crossImg);
        btn=findViewById(R.id.addQuesBtn);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isReadStoragePermissionGranted()){
                    selectImage();
                }
            }
        });

        crossImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri=null;
                img.setImageDrawable(ContextCompat.getDrawable(AddPostActivity.this, R.drawable.ic_add_a_photo_black_24dp));
                textView.setText("ছবি যোগ করুন");
                crossImg.setVisibility(View.GONE);

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // FirebaseUtilClass.getDatabase().
            }
        });
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

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                // Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                //  Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_IMG_READ);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && data!=null) {
            uri=data.getData();
            img.setImageURI(uri);
            textView.setText("ছবি যুক্ত হয়েছে...");
            crossImg.setVisibility(View.VISIBLE);
        }
        if(data==null){
            //reset();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_IMG_READ:
                //   Log.d(TAG, "External storage2");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    //     progress.dismiss();
                }
                break;


        }
    }
}

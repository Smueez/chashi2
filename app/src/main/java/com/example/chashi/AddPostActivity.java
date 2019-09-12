package com.example.chashi;

import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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

        addImage = findViewById(R.id.addImageLayout);
        quesEditText = findViewById(R.id.quesEditText);
        img = findViewById(R.id.imgPost);
        textView = findViewById(R.id.addImgMsg);
        crossImg = findViewById(R.id.crossImg);
        btn = findViewById(R.id.addQuesBtn);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReadStoragePermissionGranted()) {
                    selectImage();
                }
            }
        });

        crossImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = null;
                img.setImageDrawable(ContextCompat.getDrawable(AddPostActivity.this, R.drawable.ic_add_a_photo_black_24dp));
                textView.setText("ছবি যোগ করুন");
                crossImg.setVisibility(View.GONE);

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pushId = FirebaseUtilClass.getDatabaseReference().child("Post").push().getKey();
                writeDataToFirebase(new Ques(System.currentTimeMillis(), quesEditText.getText().toString(), pushId, FirebaseAuth.getInstance().getUid()));
            }
        });
    }

    public void writeDataToFirebase(final Ques item) {
        loading();

        Toast.makeText(AddPostActivity.this, item.getPushId(), Toast.LENGTH_SHORT).show();
        FirebaseUtilClass.getDatabaseReference().child("Post").child(item.getPushId()).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                   Toast.makeText(AddPostActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                finish();
                if (uri != null) {
                    uploadFile(uri, item.getPushId());
                } else {
                    finish();
                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                notLoading();
            }
        });

    }

    private void uploadFile(Uri imgUri, String code) {
        final ProgressDialog dialog = ProgressDialog.show(AddPostActivity.this, "",
                "অপেক্ষা করুন...", true);
        dialog.show();
        if (imgUri != null) {
            final StorageReference fileReference = FirebaseStorage.getInstance().getReference("uploads").child("Post").child(code);
            UploadTask mUploadTask;
            mUploadTask = (UploadTask) fileReference.putFile(imgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            }, 500);


                            Toast.makeText(AddPostActivity.this, "সম্পন্ন হয়েছে!", Toast.LENGTH_LONG).show();
                            finish();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            dialog.setMessage("ছবি আপলোড হচ্ছে: " + progress + "%");
                        }
                    });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void loading() {
        btn.setEnabled(false);
    }

    private void notLoading() {
        btn.setEnabled(true);
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
        if (requestCode == PICK_IMAGE && data != null) {
            uri = data.getData();
            img.setImageURI(uri);
            textView.setText("ছবি যুক্ত হয়েছে...");
            crossImg.setVisibility(View.VISIBLE);
        }
        if (data == null) {
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

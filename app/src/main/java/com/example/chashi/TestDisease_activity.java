package com.example.chashi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.io.IOException;
import java.util.List;

public class TestDisease_activity extends AppCompatActivity {
    private final int PICK_IMAGE = 1, REQ_IMG_READ = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);


        if(isReadStoragePermissionGranted()){
            selectImage();
        }
        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("model")
                .setAssetFilePath("plants/manifest.json")
                .build();
        FirebaseModelManager.getInstance().registerLocalModel(localModel);

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
        if (requestCode == PICK_IMAGE) {
            runML(data);
        }
    }

    private void runML(final Intent data) {

        FirebaseVisionOnDeviceAutoMLImageLabelerOptions labelerOptions =
                new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder()
                        .setLocalModelName("model")    // Skip to not use a local model

                        .setConfidenceThreshold(0)  // Evaluate your model in the Firebase console
                        // to determine an appropriate value.
                        .build();
        try {
            final FirebaseVisionImageLabeler labeler =
                    FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(labelerOptions);
            final Uri imageURI = data.getData();
            ImageView i=findViewById(R.id.imgevw);
            i.setImageURI(imageURI);
            final FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(this, imageURI);
            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                            for (FirebaseVisionImageLabel label: labels) {
                                String text = label.getText();
                                float confidence = label.getConfidence();
                                Toast.makeText(TestDisease_activity.this,text+" "+confidence,Toast.LENGTH_LONG).show();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TestDisease_activity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (FirebaseMLException e) {
            e.printStackTrace();
            Toast.makeText(TestDisease_activity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(TestDisease_activity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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

package com.example.chashi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

import java.io.IOException;
import java.util.List;

public class RealTimeActivity extends AppCompatActivity {
    private final int  REQ_IMG_READ=1;
    private final String LATE_BLIGHT="Potato_Late_blight",EARLY_BLIGHT="Potato_Early_blight",BACTERIAL_SPOT="Pepper_bell_Bacterial_spot";
    private CameraView cameraView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.layput_realtime);

        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("model")
                .setAssetFilePath("plants/manifest.json")
                .build();
        FirebaseModelManager.getInstance().registerLocalModel(localModel);


        cameraView=findViewById(R.id.cameraView);

        isCameratoragePermissionGranted();
        isAudioPermissionGranted()
                ;
        cameraView.setLifecycleOwner(this);
        cameraView.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
              //  Toast.makeText(RealTimeActivity.this, "s1", Toast.LENGTH_SHORT).show();
                runML(getVisionImageFromFrame(frame));

            }
        });
    }


    private void runML(final FirebaseVisionImage image) {

        FirebaseVisionOnDeviceAutoMLImageLabelerOptions labelerOptions =
                new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder()
                        .setLocalModelName("model")    // Skip to not use a local model

                        .setConfidenceThreshold(0)  // Evaluate your model in the Firebase console
                        // to determine an appropriate value.
                        .build();
        try {
            final FirebaseVisionImageLabeler labeler =
                    FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(labelerOptions);
            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {

                            String text = labels.get(0).getText();
                            // float confidence = label.getConfidence();
                            switch (text){
                                case BACTERIAL_SPOT:
                             //       showError();
                                    Toast.makeText(RealTimeActivity.this, "BACSPOT", Toast.LENGTH_SHORT).show();
                                    break;
                                case EARLY_BLIGHT:
                              //      showError();
                                    Toast.makeText(RealTimeActivity.this, "EARLY", Toast.LENGTH_SHORT).show();
                                    break;
                                case LATE_BLIGHT:
                              //      showError();
                                    Toast.makeText(RealTimeActivity.this, "LATE", Toast.LENGTH_SHORT).show();
                                    break;


                                default:
                                    Toast.makeText(RealTimeActivity.this, "OK", Toast.LENGTH_SHORT).show();
                               //     showNotError();
                            }

                            //  Toast.makeText(TestDiseaseFragment.this, text + " " + confidence, Toast.LENGTH_LONG).show();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RealTimeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (FirebaseMLException e) {
            e.printStackTrace();
            Toast.makeText(RealTimeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private FirebaseVisionImage getVisionImageFromFrame(Frame frame)  {
        //ByteArray for the captured frame
        byte[] bt = frame.getData();

        //Metadata that gives more information on the image that is to be converted to FirebaseVisionImage
        FirebaseVisionImageMetadata imageMetaData = new FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setRotation(frame.getRotation())
                .setHeight(frame.getSize().getHeight())
                .setWidth(frame.getSize().getWidth())
                .build();

        FirebaseVisionImage image = FirebaseVisionImage.fromByteArray(bt, imageMetaData);
        return image;
    }

    public boolean isCameratoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
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

    public boolean isAudioPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
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
}

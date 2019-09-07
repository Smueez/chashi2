package com.example.chashi.ui.scan;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.chashi.BottomSheetOption;
import com.example.chashi.LandingPage;
import com.example.chashi.R;
import com.example.chashi.ui.gallery.GalleryViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.text.Line;
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

public class TestDiseaseFragment extends Fragment {
    private final int PICK_IMAGE = 1, REQ_IMG_READ = 2;
    private final String LATE_BLIGHT="Potato_Late_blight",EARLY_BLIGHT="Potato_Early_blight",BACTERIAL_SPOT="Pepper_bell_Bacterial_spot";

    private Button openGlry;
    private ImageView img;
    private LinearLayout found, notFound, loading, init;
    private TestDiseaseViewModel galleryViewModel;
    private TextView diseaseName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(TestDiseaseViewModel.class);
        View root = inflater.inflate(R.layout.activity_detection, container, false);
        return root;


    }

    @Override
    public void onResume() {
        ((LandingPage) getActivity())
                .setActionBarTitle("রোগ চিহ্নিতকরণ");

        super.onResume();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        img = view.findViewById(R.id.imgevw);
        openGlry = view.findViewById(R.id.btnOpenGallery);
        diseaseName = view.findViewById(R.id.diseaseName);
        notFound = view.findViewById(R.id.noDiseaseFoundInfo);
        found = view.findViewById(R.id.diseaseFoundInfo);
        loading=view.findViewById(R.id.loadingInfo);
        init=view.findViewById(R.id.initInfo);
        reset();

        openGlry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isReadStoragePermissionGranted()) {
//
//                    selectImage();
//                    reset();
//                    showLoading();
//                }

                BottomSheetOption bottomSheetOption = new BottomSheetOption(getContext());
                bottomSheetOption.show(getChildFragmentManager(),"bottomsheet");
            }
        });


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
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                // Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                //  Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_IMG_READ);
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
            runML(data);
            openGlry.setText("পুনরায় রোগ নির্নয় করুন");
        }
        if(data==null){
            reset();
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

            img.setImageURI(imageURI);
            final FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(getContext(), imageURI);
            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels) {

                                String text = labels.get(0).getText();
                               // float confidence = label.getConfidence();
                                switch (text){
                                    case BACTERIAL_SPOT:
                                        showError();
                                        diseaseName.setText(getResources().getString(R.string.disease_spot));
                                        break;
                                    case EARLY_BLIGHT:
                                        showError();
                                        diseaseName.setText(getResources().getString(R.string.disease_agam_dhosha));
                                        break;
                                    case LATE_BLIGHT:
                                        showError();
                                        diseaseName.setText(getResources().getString(R.string.disease_morok));
                                        break;


                                        default:
                                            showNotError();
                                }

                                //  Toast.makeText(TestDiseaseFragment.this, text + " " + confidence, Toast.LENGTH_LONG).show();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (FirebaseMLException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showError(){
        found.setVisibility(View.VISIBLE);
        notFound.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        openGlry.setVisibility(View.VISIBLE);
        init.setVisibility(View.GONE);
    }

    private void showNotError(){
        found.setVisibility(View.GONE);
        notFound.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        openGlry.setVisibility(View.VISIBLE);
        init.setVisibility(View.GONE);
    }

    private void showLoading(){
        found.setVisibility(View.GONE);
        notFound.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        openGlry.setVisibility(View.GONE);
        init.setVisibility(View.GONE);
    }

    private void reset(){
        found.setVisibility(View.GONE);
        notFound.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        openGlry.setVisibility(View.VISIBLE);
        init.setVisibility(View.VISIBLE);
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

    public void onButtonClicked(String text){
        Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
    }
}

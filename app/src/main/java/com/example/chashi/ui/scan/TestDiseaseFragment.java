package com.example.chashi.ui.scan;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.chashi.BottomSheetOption;
import com.example.chashi.Description_activity;
import com.example.chashi.LandingPage;
import com.example.chashi.R;
import com.example.chashi.RealTimeActivity;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.otaliastudios.cameraview.CameraView.PERMISSION_REQUEST_CODE;

public class TestDiseaseFragment extends Fragment {
    private final int PICK_IMAGE = 1, REQ_IMG_READ = 2, PICK_REALTIME = 33, REQ_CAMERA = 3;
    private final String LATE_BLIGHT = "Potato_Late_blight", EARLY_BLIGHT = "Potato_Early_blight", BACTERIAL_SPOT = "Pepper_bell_Bacterial_spot";
    private String cropName;
    private Button openGlry, openCam, gotoProblem;
    private ImageView img;
    private LinearLayout found, notFound, loading, init;
    private TestDiseaseViewModel galleryViewModel;
    private TextView diseaseName, cropNameTextView;
    private Intent intent;
    String file_dir_final;
    int file_count = 0;
    StorageReference storageReference;
    String[] fileNameArr = {"manifest.json","dict.txt","model.tflite"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cropName = getArguments().getString("name");

        //  .setTitle();
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
        storageReference = FirebaseStorage.getInstance().getReference();
        openGlry = view.findViewById(R.id.btnOpenGallery);
        diseaseName = view.findViewById(R.id.diseaseName);
        notFound = view.findViewById(R.id.noDiseaseFoundInfo);
        found = view.findViewById(R.id.diseaseFoundInfo);
        loading = view.findViewById(R.id.loadingInfo);
        gotoProblem = view.findViewById(R.id.moreInfoButton);
        init = view.findViewById(R.id.initInfo);
        openCam = view.findViewById(R.id.btnOpenCamera);
        cropNameTextView = view.findViewById(R.id.crop_name_textView);
        reset();

        openGlry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGlry();
            }

        });

        openCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCam();
            }

        });




        intent = new Intent(getContext(), Description_activity.class);
        cropNameTextView.setText(cropName);
        //((LandingPage) getActivity()).setTitleActivity(cropName+"র রোগ চিহ্নিতকরণ");
        //   getActivity().ge.setTitle();

        // Toast.makeText(getContext(), cropName+"র রোগ চিহ্নিতকরণ", Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
              //  Toast.makeText(getContext(),"check permission ok",Toast.LENGTH_SHORT).show();
            } else {
                requestPermission(); // Code for permission

            }
        }
        else
        {

            // Code for Below 23 API Oriented Device
            // Do next code
        }
        //download start....
        Log.d("ok", "download: 1");

        //notificationDownload();
        File rootPath = new File(Environment.getExternalStorageDirectory(), "চাষি");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File cropFile = new File(rootPath,cropName);
        file_dir_final = cropFile.toString();
        Log.d("disease detection", "onViewCreated: "+file_dir_final);
        if (!cropFile.exists()) {
            cropFile.mkdir();
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("(১/৩) ডাউনলোড হচ্ছে...");
            progressDialog.show();

            final File localFile = new File(cropFile, fileNameArr[0]);


            storageReference.child("models").child(cropName).child(fileNameArr[0]).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Log.e("firebase ",";local tem file created  created " +localFile.toString());
                    //  updateDb(timestamp,localFile.toString(),position);
                    progressDialog.dismiss();
                    //Toast.makeText(getContext(),"ডাউনলোড শেষ!",Toast.LENGTH_LONG).show();
                    Log.d("downloaded file", "onSuccess: downloaded! " + fileNameArr[0]);
                    Log.d("ok", "download: 1");
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("(২/৩) ডাউনলোড হচ্ছে...");
                    progressDialog.show();
                    //notificationDownload();
                /*File rootPath = new File(Environment.getExternalStorageDirectory(), "চাষি");
                if(!rootPath.exists()) {
                    rootPath.mkdirs();
                }

                final File cropFile = new File(rootPath,cropName);
                file_dir_final = cropFile.toString();
                if (!cropFile.exists()){
                    cropFile.mkdir();
                }
                else {
                    return;
                }*/
                    final File localFile = new File(cropFile, fileNameArr[1]);


                    storageReference.child("models").child(cropName).child(fileNameArr[1]).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Log.e("firebase ",";local tem file created  created " +localFile.toString());
                            //  updateDb(timestamp,localFile.toString(),position);
                            progressDialog.dismiss();
                            //Toast.makeText(getContext(),"ডাউনলোড শেষ!",Toast.LENGTH_LONG).show();
                            Log.d("downloaded file", "onSuccess: downloaded! " + fileNameArr[1]);
                            Log.d("ok", "download: 1");
                            final ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setTitle("(৩/৩) ডাউনলোড হচ্ছে... ");
                            progressDialog.show();
                            //notificationDownload();
                        /*File rootPath = new File(Environment.getExternalStorageDirectory(), "চাষি");
                        if(!rootPath.exists()) {
                            rootPath.mkdirs();
                        }

                        File cropFile = new File(rootPath,cropName);
                        file_dir_final = cropFile.toString();
                        if (!cropFile.exists()){
                            cropFile.mkdir();
                        }
                        else {
                            return;
                        }*/
                            final File localFile = new File(cropFile, fileNameArr[2]);


                            storageReference.child("models").child(cropName).child(fileNameArr[2]).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Log.e("firebase ",";local tem file created  created " +localFile.toString());
                                    //  updateDb(timestamp,localFile.toString(),position);
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "ডাউনলোড শেষ!", Toast.LENGTH_LONG).show();
                                    Log.d("downloaded file", "onSuccess: downloaded! " + fileNameArr[2]);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Log.e("firebase ", ";local tem file not created  created " + exception.toString());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e("firebase ", ";local tem file not created  created " + exception.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("firebase ", ";local tem file not created  created " + exception.toString());
                }
            });
        }
        FirebaseLocalModel localModel = new FirebaseLocalModel.Builder("model"+cropName)
                .setFilePath(file_dir_final + "/manifest.json")
                .build();
        FirebaseModelManager.getInstance().

                registerLocalModel(localModel);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
   /* public void download(final String filename){
        Log.d("ok", "download: 1");
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Downloading...");
        progressDialog.show();
        //notificationDownload();
        File rootPath = new File(Environment.getExternalStorageDirectory(), "চাষি");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        File cropFile = new File(rootPath,cropName);
        file_dir_final = cropFile.toString();
        if (!cropFile.exists()){
            cropFile.mkdir();
        }
        else {
            return;
        }
        final File localFile = new File(cropFile,filename);


        storageReference.child("models").getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Log.e("firebase ",";local tem file created  created " +localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
                progressDialog.dismiss();
                Toast.makeText(getContext(),"ডাউনলোড শেষ!",Toast.LENGTH_LONG).show();
                file_count++;
                Log.d("downloaded file", "onSuccess: downloaded! "+filename);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ",";local tem file not created  created " +exception.toString());
            }
        });
    }*/
    public void openGlry() {
        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE, REQ_IMG_READ)) {
            selectImage();
            reset();
            showLoading();
        }
    }

    public void openCam() {
        if (isPermissionGranted(Manifest.permission.CAMERA, REQ_CAMERA)) {
            showLoading();
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, PICK_REALTIME);
            }
        }
        //   Intent intent1 = new Intent(getActivity(), RealTimeActivity.class);
        //  startActivityForResult(intent1, PICK_REALTIME);
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    public boolean isPermissionGranted(String permission, final int REQ_CODE) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(permission)
                    == PackageManager.PERMISSION_GRANTED) {
                // Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                //  Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, REQ_CODE);
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
            if (data != null) {
                runML(data.getData());
                openGlry.setText("পুনরায় রোগ নির্নয় করুন");
            } else {
                reset();
            }

        } else if (requestCode == PICK_REALTIME) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            runML(imageBitmap);
            // imageOfCamera.setImageBitmap(imageBitmap);
            /*   realtime activity
            if (resultCode == Activity.RESULT_OK) {
                //Toast.makeText(getContext(), "laa", Toast.LENGTH_LONG).show();
                String result = data.getStringExtra("result");
                setMsg(result);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }*/
        }
    }


    private void runML(final Bitmap data) {
        FirebaseVisionImage image = null;

        image = FirebaseVisionImage.fromBitmap(data);
        img.setImageBitmap(data);
        doML(image);
    }

    private void runML(final Uri data) {

        FirebaseVisionImage image = null;
        try {
            image = FirebaseVisionImage.fromFilePath(getContext(), data);
            img.setImageURI(data);
            doML(image);
        } catch (IOException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }


    }

    private void doML(FirebaseVisionImage image) {
        FirebaseVisionOnDeviceAutoMLImageLabelerOptions labelerOptions =
                new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder()
                        .setLocalModelName("model"+cropName)    // Skip to not use a local model

                        .setConfidenceThreshold(0)  // Evaluate your model in the Firebase console
                        // to determine an appropriate value.
                        .build();
        try {
            final FirebaseVisionImageLabeler labeler =
                    FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(labelerOptions);
            //    final Uri imageURI = data.getData();


            labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                @Override
                public void onSuccess(List<FirebaseVisionImageLabel> labels) {

                    String text = labels.get(0).getText();
                    setMsg(text);
                    // float confidence = label.getConfidence();


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
        }
    }

    private void setMsg(final String text) {
        if (text.equals(getResources().getString(R.string.healthy))) {
            showNotError();
        } else {
            showError();

            final String[] str = text.split("_");
            diseaseName.setText(str[1]);


            gotoProblem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("item_name", str[0]);
                    intent.putExtra("disease_name", str[1]);
                    startActivity(intent);
                }
            });
        }

        /*
        switch (text) {
            case BACTERIAL_SPOT:
                showError();
                diseaseName.setText(getResources().getString(R.string.disease_spot));
                gotoProblem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case EARLY_BLIGHT:
                showError();
                diseaseName.setText(getResources().getString(R.string.disease_agam_dhosha));
                gotoProblem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent.putExtra("disease_name", "আগাম ধ্বসা");
                        intent.putExtra("item_name", "আলু");
                        intent.putExtra("image_url", "https://cropscience.bayer.co.uk/media/102361687/early-blight-potatoes.jpg");
                        startActivity(intent);
                    }
                });
                break;
            case LATE_BLIGHT:
                showError();
                diseaseName.setText(getResources().getString(R.string.disease_morok));
                gotoProblem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        intent.putExtra("disease_name", "মড়ক রোগ");
                        intent.putExtra("item_name", "আলু");
                        intent.putExtra("image_url", "http://ais.portal.gov.bd/sites/default/files/files/ais.portal.gov.bd/page/0fd1e9d1_a532_4ebd_a38d_542efb9ac3ee/LBleaf2INIA_0.jpg");
                        startActivity(intent);
                    }
                });
                break;


            default:
                showNotError();


        }*/
    }

    private void showError() {
        found.setVisibility(View.VISIBLE);
        notFound.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        openGlry.setVisibility(View.VISIBLE);
        init.setVisibility(View.GONE);
    }

    private void showNotError() {
        found.setVisibility(View.GONE);
        notFound.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        openGlry.setVisibility(View.VISIBLE);
        init.setVisibility(View.GONE);
    }

    private void showLoading() {
        found.setVisibility(View.GONE);
        notFound.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        openGlry.setVisibility(View.GONE);
        init.setVisibility(View.GONE);
    }

    private void reset() {
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

    public void onButtonClicked(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }


}

package com.example.chashi;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    Button button_changePic;
    CircleImageView imageView_propic;
    TextView textView_phn, textView_name;
    EditText editText_name;
    LinearLayout linearLayout_edit;
    Button edit_name, upload_pic;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private final static int result_loead_image = 1;
    String TAG = "profile activity", phon_no_str, name_str, img_url_str;
    Uri uri, download_uri;
    FirebaseAuth auth;
    FirebaseUser user;
    ListView listView_orders;
    List<Order_list> orderLists;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orderLists = new ArrayList<>();
        listView_orders = view.findViewById(R.id.order_history);
        button_changePic = view.findViewById(R.id.changepic);
        imageView_propic = view.findViewById(R.id.profile_image);
        textView_name = view.findViewById(R.id.person_name);
        textView_phn = view.findViewById(R.id.phn_number);
        editText_name = view.findViewById(R.id.edit_name);
        linearLayout_edit = view.findViewById(R.id.edit_name_layout);
        linearLayout_edit.setVisibility(View.GONE);
        edit_name = view.findViewById(R.id.change_name);
        upload_pic = view.findViewById(R.id.upload_pic);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            phon_no_str = user.getPhoneNumber();
        }
        else {
            Toast.makeText(getContext(),"Log in first",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(),Login_activity.class);
            startActivity(intent);
            return;
        }
        textView_phn.setText(phon_no_str);


        upload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //upload pic
                storageReference.child("profile/" + phon_no_str + "." + getFileExtention(uri)).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // download_uri = taskSnapshot.getUploadSessionUri();

                        Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                        firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                download_uri = uri;
                                FirebaseUtilClass.getDatabaseReference().child("profile").child(phon_no_str).child("image_url").setValue(download_uri.toString());
                                Picasso.get().load(download_uri.toString()).into(imageView_propic);

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        });
        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //edit name
                String new_name = edit_name.getText().toString().trim();
                if (!new_name.isEmpty()) {
                    FirebaseUtilClass.getDatabaseReference().child("profile").child(phon_no_str).child("name").setValue(new_name);
                    linearLayout_edit.setVisibility(View.GONE);
                    textView_name.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Please write a valid name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textView_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show edit option
                textView_name.setVisibility(View.GONE);
                linearLayout_edit.setVisibility(View.VISIBLE);
            }
        });
        button_changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change photo here
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, result_loead_image);
            }
        });
    }



    private String getFileExtention(Uri imguri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imguri));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (user != null) {
            phon_no_str = user.getPhoneNumber();
        }
        else {
            Toast.makeText(getContext(),"Log in first",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(),Login_activity.class);
            startActivity(intent);
            return;
        }
        FirebaseUtilClass.getDatabaseReference().child("profile").child(phon_no_str).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                img_url_str = dataSnapshot.child("image_url").getValue(String.class);
                name_str = dataSnapshot.child("name").getValue(String.class);

                textView_name.setText(name_str);
                edit_name.setText(name_str);
                Picasso.get().load(img_url_str).into(imageView_propic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseUtilClass.getDatabaseReference().child("profile").child(phon_no_str).child("personal_orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listView_orders.setAdapter(null);
                orderLists.clear();
               for (DataSnapshot ds : dataSnapshot.getChildren()){
                   Order_list order_list = ds.getValue(Order_list.class);
                   orderLists.add(order_list);
               }
               Order_list_adapter orderListAdapter = new Order_list_adapter(getActivity(),orderLists);
               listView_orders.setAdapter(orderListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == result_loead_image && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            imageView_propic.setImageURI(uri);
            Log.d(TAG, "onActivityResult: Pic is loading!");
        }
    }

}

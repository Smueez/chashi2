package com.example.chashi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuesActivity extends AppCompatActivity {
    private TextView quesTitle;
    private EditText commentEditText;
    private Button commentButton;
    private ImageView img;
    private String str;
    private RecyclerView recyclerView;
    private List<Comments> itemList = new ArrayList<>();
    private InventoryItemAdapter2 mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ques);
        quesTitle = findViewById(R.id.questitle);
        img = findViewById(R.id.img);
        commentEditText = findViewById(R.id.commentEditText);
        commentButton = findViewById(R.id.commentBtn);
        String getObj;
        getObj = getIntent().getStringExtra("itemData");
        str = getIntent().getStringExtra("itemId");
        quesTitle.setText(getObj);


        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    writeDataToFirebase(new Comments(FirebaseAuth.getInstance().getCurrentUser().getUid(), commentEditText.getText().toString(), System.currentTimeMillis()));
                } else {
                    new AlertDialog.Builder(QuesActivity.this)
                            .setTitle("তথ্য")
                            .setMessage("প্রশ্ন জিজ্ঞেস করতে আপনার ফোন নাম্বার দিতে হবে। আপনি কি রাজি?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent k = new Intent(QuesActivity.this, Login_activity.class);
                                    startActivity(k);
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        initializeRecyclerView();
    }


    private void initializeRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewComments);

        mAdapter = new InventoryItemAdapter2(itemList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        FirebaseUtilClass.getDatabaseReference().child("Post").child(str).child("comments").orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    itemList.add(dsp.getValue(Comments.class)); //add result into array list
                }
                mAdapter.notifyDataSetChanged();
                // refreshlayout.setRefreshing(false);
                if (itemList.size() == 0) {
                    // noIngredientsTextView.setVisibility(View.VISIBLE);
                } else {
                    //  noIngredientsTextView.setVisibility(View.GONE);
                }
                //   Toast.makeText(getContext(), "Changed something", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //   Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //  refreshlayout.setRefreshing(false);
            }
        });
    }

    public void writeDataToFirebase(final Comments item) {
        //loading();

        // Toast.makeText(AddPostActivity.this, item.getPushId(), Toast.LENGTH_SHORT).show();
        FirebaseUtilClass.getDatabaseReference().child("Post").child(str).child("comments").push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(QuesActivity.this, "Commented", Toast.LENGTH_SHORT).show();
                commentEditText.setText("");
                // finish();


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(QuesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                //   notLoading();
            }
        });

    }


}

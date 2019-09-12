package com.example.chashi.ui.forums;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chashi.AddPostActivity;
import com.example.chashi.FirebaseUtilClass;
import com.example.chashi.InventoryItemAdapter;
import com.example.chashi.LandingPage;
import com.example.chashi.Login_activity;
import com.example.chashi.Ques;
import com.example.chashi.R;
import com.example.chashi.ui.gallery.GalleryViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ForumFragment extends Fragment {
    private ForumViewModel forumViewModel;
    private TextView addQuesTextView;
    private RecyclerView recyclerView;
    private List<Ques> itemList = new ArrayList<>();
    private InventoryItemAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        forumViewModel =
                ViewModelProviders.of(this).get(ForumViewModel.class);
        View root = inflater.inflate(R.layout.fragment_forum, container, false);
        return root;
    }

    @Override
    public void onResume() {
        ((LandingPage) getActivity())
                .setActionBarTitle("কৃষি জিজ্ঞাসা");

        super.onResume();

    }

    private void initializeRecyclerView() {
        recyclerView = getView().findViewById(R.id.recyclerViewComments);

        mAdapter = new InventoryItemAdapter(itemList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addQuesTextView = view.findViewById(R.id.addQuesTextView);
        recyclerView = view.findViewById(R.id.recyclerViewComments);

        addQuesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    new AlertDialog.Builder(getContext())
                            .setTitle("তথ্য")
                            .setMessage("প্রশ্ন জিজ্ঞেস করতে আপনার ফোন নাম্বার দিতে হবে। আপনি কি রাজি?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent k = new Intent(getActivity(), Login_activity.class);
                                    startActivity(k);
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else{
                    Intent k = new Intent(getActivity(), AddPostActivity.class);
                    startActivity(k);
                }

            }
        });
        initializeRecyclerView();
            readDataFromFirebase();
    }

    public void readDataFromFirebase() {
        FirebaseUtilClass.getDatabaseReference().child("Post").orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    itemList.add(dsp.getValue(Ques.class)); //add result into array list
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
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
              //  refreshlayout.setRefreshing(false);
            }
        });
    }
}

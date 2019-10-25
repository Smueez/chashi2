package com.example.chashi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.chashi.ui.scan.TestDiseaseFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ScanDiseaseCropsFragment extends Fragment {
    ListView recyclerView;
    List<Item_list> item_lists;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan_disease_crops, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.item_recycleView);
        item_lists = new ArrayList<>();
        item_list_function();


        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Bundle bundle = new Bundle();
                bundle.putString("name", item_lists.get(i).getName());
                Fragment newFragment = new TestDiseaseFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.nav_host_fragment, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

/*
                    item_name = item_lists.get(i).getName();
                    //image_url_selected= item_lists.get(i).getImage_url();
                    Log.d(TAG, "onItemClick: " + item_name);
                    textView_highlight.setText(item_name + " এর রোগ");
                    disease_list_function(item_name);
                    item_tabbed = false;*/


            }
        });
    }

    public void item_list_function() {
        recyclerView.setAdapter(null);
        item_lists.clear();
        FirebaseUtilClass.getDatabaseReference().child("detect").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Item_list itemList = ds.getValue(Item_list.class);
                    item_lists.add(itemList);
                }

                Item_list_adapter item_list_adapter = new Item_list_adapter(getActivity(), item_lists);
                recyclerView.setAdapter(item_list_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
             //   Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}

package com.example.chashi.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.chashi.Product_category;
import com.example.chashi.R;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private LinearLayout seeds,fert,eqp,inst;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        fert = view.findViewById(R.id.catagory_fert);
        seeds = view.findViewById(R.id.catagory_seeds);
        eqp = view.findViewById(R.id.catagory_eqp);
        inst = view.findViewById(R.id.catagory_inst);

        fert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Product_category.class);
                intent.putExtra("type","fertilizer");
                startActivity(intent);
            }
        });

        seeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Product_category.class);
                intent.putExtra("type","seeds");
                startActivity(intent);
            }
        });

        eqp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Product_category.class);
                intent.putExtra("type","equipment");
                startActivity(intent);
            }
        });

        inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Product_category.class);
                intent.putExtra("type","insecticide");
                startActivity(intent);
            }
        });


        super.onViewCreated(view, savedInstanceState);
    }
}
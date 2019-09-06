package com.example.chashi.ui.forums;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chashi.AddPostActivity;
import com.example.chashi.R;
import com.example.chashi.ui.gallery.GalleryViewModel;

public class ForumFragment extends Fragment {
    private ForumViewModel forumViewModel;
    private TextView addQuesTextView;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        forumViewModel =
                ViewModelProviders.of(this).get(ForumViewModel.class);
        View root = inflater.inflate(R.layout.fragment_forum, container, false);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        addQuesTextView = view.findViewById(R.id.addQuesTextView);
        recyclerView = view.findViewById(R.id.recyclerViewComments);

        addQuesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(getActivity(), AddPostActivity.class);
                startActivity(k);
            }
        });


    }
}

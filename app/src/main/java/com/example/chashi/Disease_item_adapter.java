package com.example.chashi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.annotations.Nullable;
import java.util.List;

import androidx.annotation.NonNull;

public class Disease_item_adapter extends ArrayAdapter<Disease_item> {
    private Activity context;
    private List<Disease_item> list;
    String list_img;

    public Disease_item_adapter(Activity context,List<Disease_item>list){
        super(context,R.layout.disease_item_layout,list);
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View mylistview_disease = inflater.inflate(R.layout.disease_item_layout,null,true);

        TextView textView = mylistview_disease.findViewById(R.id.fertiliser_name);


        Disease_item disease_item = list.get(position);

        textView.setText(disease_item.getDisease_name());

        return mylistview_disease;
    }
}

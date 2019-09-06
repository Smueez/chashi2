package com.example.chashi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;

public class Disease_list_adapter extends ArrayAdapter<Disease_list> {
    private Activity context;
    private List<Disease_list> list;
    String list_img;

    public Disease_list_adapter(Activity context,List<Disease_list>list){
        super(context,R.layout.disease_list_view,list);
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View mylistview_disease = inflater.inflate(R.layout.disease_list_view,null,true);

        TextView textView = mylistview_disease.findViewById(R.id.disease_name);
        ImageView imageView = mylistview_disease.findViewById(R.id.disease_img);
        TextView textView_description = mylistview_disease.findViewById(R.id.dscription);

        Disease_list disease_list = list.get(position);

        textView.setText(disease_list.getDisease_name());
        textView_description.setText(disease_list.getDescription());
        list_img = disease_list.getDisease_image_url();

        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(list_img).getContent());
            imageView.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return mylistview_disease;
    }
}

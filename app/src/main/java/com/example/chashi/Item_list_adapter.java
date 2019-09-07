package com.example.chashi;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.annotations.Nullable;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;

public class Item_list_adapter extends ArrayAdapter<Item_list> {

    private Activity context;
    private List<Item_list> list;
    String list_img;

    public Item_list_adapter(Activity context,List<Item_list>list){
        super(context,R.layout.item_list_view,list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View mylistview = inflater.inflate(R.layout.item_list_view,null,true);

        TextView textView = mylistview.findViewById(R.id.textView_item_name);
        ImageView imageView = mylistview.findViewById(R.id.item_img);

        Item_list item_list = list.get(position);

        textView.setText(item_list.getName());

        list_img = item_list.getImage_url();

        Picasso.get().load(list_img).into(imageView);

        return mylistview;
    }
}

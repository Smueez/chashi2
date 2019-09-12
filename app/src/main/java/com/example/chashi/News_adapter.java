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

public class News_adapter extends ArrayAdapter<News> {
    private Activity context;
    private List<News> list;
    String list_img;
    public News_adapter(Activity context,List<News>list){
        super(context,R.layout.news_layout,list);
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View mylistview_news = inflater.inflate(R.layout.news_layout,null,true);

        TextView textView = mylistview_news.findViewById(R.id.news_heading);
        TextView textView_description = mylistview_news.findViewById(R.id.news_description);

        News news = list.get(position);

        textView.setText(news.getHeadings());
        textView_description.setText(news.getDescription());


        return mylistview_news;
    }
}

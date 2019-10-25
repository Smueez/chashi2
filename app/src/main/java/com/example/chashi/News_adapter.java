package com.example.chashi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.annotations.Nullable;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class News_adapter extends PagerAdapter {
    private Context context;
    private List<News> list;
   // String list_img;
    LayoutInflater layoutInflater;
    public News_adapter(Context context,List<News>list){
        //super(context,R.layout.news_layout,list);
        this.context = context;
        this.list = list;
    }
    /*@NonNull
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
    }*/

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View mylistview_news = layoutInflater.inflate(R.layout.news_layout,container,false);
        TextView textView = mylistview_news.findViewById(R.id.news_heading);
        TextView textView_description = mylistview_news.findViewById(R.id.news_description);

        News news = list.get(position);

        textView.setText(news.getHeadings());
        textView_description.setText(news.getDescription());

        container.addView(mylistview_news,0);

        return mylistview_news;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}

package com.example.chashi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> itemList;
    private Context context;

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView textView_heading,textView_description;
        ImageView imageView_head;
        LinearLayout cardView_news;
        News news;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_description = itemView.findViewById(R.id.news_description);
            textView_heading = itemView.findViewById(R.id.news_heading);
            imageView_head = itemView.findViewById(R.id.news_image);
            cardView_news = itemView.findViewById(R.id.news_layout);

        }
        public News getItem() {
            return news;
        }

        public void setItem(News news) {
            this.news = news;
        }
    }


    public NewsAdapter(List<News> newsList, Context context) {
        this.itemList = newsList;
        // this.itemListFiltered = itemList;
        this.context = context;
    }
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_layout, parent, false);

        return new NewsViewHolder(itemView);
    }
    String news_link,image_url;
    @Override
    public void onBindViewHolder(NewsAdapter.NewsViewHolder holder, int position) {
        final News item = itemList.get(position);
        holder.textView_heading.setText(item.getHeadings());
        holder.textView_description.setText(String.valueOf(item.getDescription()));

        image_url = item.getImg_url();
        news_link = item.getLinks();
        if (image_url != null){
            Picasso.get().load(image_url).into(holder.imageView_head);
        }
        holder.cardView_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse(news_link));
            }
        });
        holder.setItem(item);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

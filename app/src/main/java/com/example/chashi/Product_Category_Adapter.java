package com.example.chashi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Product_Category_Adapter extends RecyclerView.Adapter<Product_Category_Adapter.CategoryViewHolder>{

    private List<Product_item> itemList;
    private Context context;
    private MainActivity mainActivity;

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView name,price;
        private ImageView image;
        private LinearLayout container;
        private Product_item item;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            image = itemView.findViewById(R.id.product_image);
            container = itemView.findViewById(R.id.product_container);

        }

        public Product_item getItem() {
            return item;
        }

        public void setItem(Product_item item) {
            this.item = item;
        }
    }

    public Product_Category_Adapter(List<Product_item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public Product_Category_Adapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_layout,viewGroup,false);

        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Product_Category_Adapter.CategoryViewHolder categoryViewHolder, int i) {
        final Product_item item = itemList.get(i);
        categoryViewHolder.name.setText(item.getName());
        categoryViewHolder.price.setText(item.getPrice());
        Picasso.get().load(item.getImage()).into(categoryViewHolder.image);
        categoryViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Purchase.class);
                intent.putExtra("itemData",item);
                context.startActivity(intent);
            }
        });

        categoryViewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

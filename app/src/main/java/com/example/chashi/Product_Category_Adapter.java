package com.example.chashi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import iammert.com.expandablelib.ExpandableLayout;
import iammert.com.expandablelib.Section;

public class Product_Category_Adapter extends RecyclerView.Adapter<Product_Category_Adapter.CategoryViewHolder>{

    private List<Product_item> itemList;
    private List<SubCatagory> subCatagories;
    private Context context;
    private MainActivity mainActivity;

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ExpandableLayout expandableLayout;
        private SubCatagory item;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
        }

        public SubCatagory getItem() {
            return item;
        }

        public void setItem(SubCatagory item) {
            this.item = item;
        }
    }

    public Product_Category_Adapter(List<SubCatagory> subCatagories, Context context) {
        this.subCatagories = subCatagories;



        this.context = context;
    }

    @NonNull
    @Override
    public Product_Category_Adapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_subcatagory,viewGroup,false);

        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Product_Category_Adapter.CategoryViewHolder categoryViewHolder, int i) {
        final SubCatagory item = subCatagories.get(i);

        categoryViewHolder.expandableLayout.setRenderer(new ExpandableLayout.Renderer<SubCatagory,Product_item>() {
            @Override
            public void renderParent(View view, SubCatagory subCatagory, boolean b, int i) {
                ((TextView)view.findViewById(R.id.sub_cat_name)).setText(subCatagory.getSub_cat_name());
                ((ImageView)view.findViewById(R.id.sub_cat_image)).setImageResource(R.drawable.ic_potato);
                ((ImageView)view.findViewById(R.id.sub_cat_arrow)).setImageResource(b?R.drawable.ic_up_arrow:R.drawable.ic_angle_arrow_down);
            }

            @Override
            public void renderChild(View view, final Product_item product_item, int i, int i1) {
                LinearLayout linearLayout = view.findViewById(R.id.product_layout);
                ImageView imageView = (ImageView)view.findViewById(R.id.product_child_image);
                Picasso.get().load(product_item.getImage()).into(imageView);
                ((TextView)view.findViewById(R.id.product_child_name)).setText(product_item.getName());
                ((TextView)view.findViewById(R.id.product_child_desc)).setText(product_item.getDesc());
                ((TextView)view.findViewById(R.id.product_child_price)).setText(product_item.getPrice());

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context,Purchase.class);
                        intent.putExtra("itemData",product_item);
                        context.startActivity(intent);
                    }
                });

            }
        });

        categoryViewHolder.expandableLayout.addSection(getSection(item));

        categoryViewHolder.setItem(item);
    }

    private Section<SubCatagory,Product_item> getSection(SubCatagory subCatagory){
        Section<SubCatagory,Product_item> section = new Section<>();
        List<Product_item> product_items = subCatagory.getProduct_items();
        section.parent = subCatagory;
        section.children.addAll(product_items);
        return section;
    }

    @Override
    public int getItemCount() {
        return subCatagories.size();
    }
}

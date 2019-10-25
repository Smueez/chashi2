package com.example.chashi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class InsecticideAdapter extends RecyclerView.Adapter<InsecticideAdapter.ProductViewHolder> {


    private List<Product_item> itemList;
    private Context context;
    //  private List<InventoryItem> itemListFiltered;

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView codeTextView, inMachineTextView;
        private ImageView img;
        private Product_item item;
        private LinearLayout parent;

        public ProductViewHolder(View view) {
            super(view);
            codeTextView = view.findViewById(R.id.insecticideName);
            inMachineTextView = view.findViewById(R.id.amount);
            img=view.findViewById(R.id.img);

            //     parent=view.findViewById(R.id.dataContainer);
        }

        public Product_item getItem() {
            return item;
        }

        public void setItem(Product_item item) {
            this.item = item;
        }
    }


    public InsecticideAdapter(List<Product_item> moviesList, Context context) {
        this.itemList = moviesList;
        // this.itemListFiltered = itemList;
        this.context = context;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_insacticide, parent, false);

        return new ProductViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final Product_item item = itemList.get(position);
        holder.codeTextView.setText(item.getName());
        holder.inMachineTextView.setText(String.valueOf(item.getPrice()));
        Picasso.get().load(item.getImage()).into(holder.img);

        holder.setItem(item);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}


package com.example.chashi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InventoryItemAdapter2 extends RecyclerView.Adapter<InventoryItemAdapter2.InventoryViewHolder>   {
    private List<Comments> itemList;
    private Context context;

    public class InventoryViewHolder extends RecyclerView.ViewHolder {
        private TextView codeTextView;
        private Comments item;
        private LinearLayout parent;

        public InventoryViewHolder(View view) {
            super(view);
            codeTextView = view.findViewById(R.id.textViewAnsTitle);

        }

        public Comments getItem() {
            return item;
        }

        public void setItem(Comments item) {
            this.item = item;
        }
    }


    public InventoryItemAdapter2(List<Comments> moviesList, Context context) {
        this.itemList = moviesList;

        this.context=context;
    }

    @Override
    public InventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_comment2, parent, false);

        return new InventoryViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(InventoryViewHolder holder, int position) {
        final Comments item = itemList.get(position);
        holder.codeTextView.setText(item.getMsg());



        holder.setItem(item);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

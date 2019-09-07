package com.example.chashi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class InventoryItemAdapter extends RecyclerView.Adapter<InventoryItemAdapter.InventoryViewHolder>   {
    private List<Ques> itemList;
    private Context context;

    public class InventoryViewHolder extends RecyclerView.ViewHolder {
        private TextView codeTextView;
        private Ques item;
        private LinearLayout parent;

        public InventoryViewHolder(View view) {
            super(view);
            codeTextView = view.findViewById(R.id.textViewAnsTitle);

        }

        public Ques getItem() {
            return item;
        }

        public void setItem(Ques item) {
            this.item = item;
        }
    }


    public InventoryItemAdapter(List<Ques> moviesList, Context context) {
        this.itemList = moviesList;

        this.context=context;
    }

    @Override
    public InventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_comment, parent, false);

        return new InventoryViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(InventoryViewHolder holder, int position) {
        final Ques item = itemList.get(position);
        holder.codeTextView.setText(item.getQues());



        holder.setItem(item);
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

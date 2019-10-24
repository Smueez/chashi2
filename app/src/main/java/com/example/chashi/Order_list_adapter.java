package com.example.chashi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.annotations.Nullable;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;

public class Order_list_adapter extends ArrayAdapter<Order_list> {
    private Activity context;
    private List<Order_list> list;
    String list_img;
    public Order_list_adapter(Activity context,List<Order_list>list){
        super(context,R.layout.order_list,list);
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View mylistview_order = inflater.inflate(R.layout.order_list,null,true);

        TextView textView_order_name = mylistview_order.findViewById(R.id.product_name_order);
        ImageView imageView = mylistview_order.findViewById(R.id.product_img);
        TextView textView_cond = mylistview_order.findViewById(R.id.order_present);
        TextView textView_loc = mylistview_order.findViewById(R.id.order_location);
        TextView textView_date = mylistview_order.findViewById(R.id.order_date_placement);
        TextView textView_rcv = mylistview_order.findViewById(R.id.order_date_receive);
        TextView textView_cost = mylistview_order.findViewById(R.id.order_cost);
        TextView textView_quantity = mylistview_order.findViewById(R.id.order_quantity);


        Order_list orderList = list.get(position);

        textView_cond.setText(orderList.getOrder_process());
        textView_order_name.setText(orderList.getOrder_product());
        textView_loc.setText(orderList.getOrder_loc());
        textView_rcv.setText(orderList.getOrder_rcv());
        textView_date.setText(orderList.getOrder_date());
        textView_quantity.setText(orderList.getOrder_quantity());
        textView_cost.setText(orderList.getOrder_cost());
        list_img = orderList.getOrder_img();


        Picasso.get().load(list_img).into(imageView);


        return mylistview_order;
    }
}

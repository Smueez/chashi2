package com.example.chashi;

public class Order_list {
    String order_img,order_product,order_process,order_quantity,order_cost,order_date,order_rcv,order_loc;
    Order_list(){}

    Order_list(String order_img, String order_product, String order_process, String order_quantity, String order_cost, String order_date, String order_rcv, String order_loc){
        this.order_cost = order_cost;
        this.order_date = order_date;
        this.order_img = order_img;
        this.order_loc = order_loc;
        this.order_process = order_process;
        this.order_product = order_product;
        this.order_quantity = order_quantity;
        this.order_rcv = order_rcv;
    }

    public String getOrder_cost() {
        return order_cost;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getOrder_img() {
        return order_img;
    }

    public String getOrder_loc() {
        return order_loc;
    }

    public String getOrder_process() {
        return order_process;
    }

    public String getOrder_product() {
        return order_product;
    }

    public String getOrder_quantity() {
        return order_quantity;
    }

    public String getOrder_rcv() {
        return order_rcv;
    }

}

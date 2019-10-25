package com.example.chashi;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

public class SubCatagory {

    private String sub_cat_name;
    private int icon;
    List<Product_item> product_items = new ArrayList<>();

    public SubCatagory() {
    }

    public SubCatagory(String sub_cat_name, int icon) {
        this.sub_cat_name = sub_cat_name;
        this.icon = icon;
    }

    public List<Product_item> getProduct_items() {
        return product_items;
    }

    public void setProduct_items(List<Product_item> product_items) {
        this.product_items = product_items;
    }

    public String getSub_cat_name() {
        return sub_cat_name;
    }

    public void setSub_cat_name(String sub_cat_name) {
        this.sub_cat_name = sub_cat_name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}

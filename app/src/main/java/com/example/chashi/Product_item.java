package com.example.chashi;

import java.io.Serializable;

public class Product_item implements Serializable {

   private String desc;
   private String name;
   private String price;
   private String image;

    public Product_item(String desc, String name, String price, String image) {
        this.desc = desc;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Product_item() {
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

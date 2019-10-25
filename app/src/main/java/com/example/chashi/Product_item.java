package com.example.chashi;

import java.io.Serializable;

public class Product_item implements Serializable {

    private String id;
    private String desc;
    private String name;
    private String price;
    private String image;
    private String quantity;

    public Product_item(String id,String desc, String name, String price, String image,String quantity) {
        this.id = id;
        this.desc = desc;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }



    public Product_item() {
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

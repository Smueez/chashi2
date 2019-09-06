package com.example.chashi;

public class Item_list {
    String name,image_url;

    Item_list(){}

    Item_list(String name,String image_url){
        this.name = name;
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public String getImage_url() {
        return image_url;
    }
}

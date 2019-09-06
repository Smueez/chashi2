package com.example.chashi;

public class Disease_list {
    String item_name, disease_name, disease_image_url, description;
    Disease_list(){}
    Disease_list(String item_name, String disease_image_url, String disease_name, String description){
        this.item_name = item_name;
        this.disease_image_url = disease_image_url;
        this.disease_name = disease_name;
        this.description = description;
    }

    public String getDisease_image_url() {
        return disease_image_url;
    }

    public String getDisease_name() {
        return disease_name;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getDescription() {
        return description;
    }
}

package com.example.chashi;

public class Category {

    private String name;
    private String type;
    private String URL;

    public Category(String name, String type, String URL) {
        this.name = name;
        this.type = type;
        this.URL = URL;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}

package com.example.chashi;

public class News {
    String headings,description,links,img_url;

    public News(String headings, String description, String links,String img_url) {
        this.headings = headings;
        this.description = description;
        this.links = links;
        this.img_url = img_url;
    }

    public News() {
    }

    public void setHeadings(String headings) {
        this.headings = headings;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getDescription() {
        return description;
    }

    public String getHeadings() {
        return headings;
    }

    public String getLinks() {
        return links;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}

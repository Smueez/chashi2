package com.example.chashi;

public class News {
    String headings,description,links;

    public News(String headings, String description, String links) {
        this.headings = headings;
        this.description = description;
        this.links = links;
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
}

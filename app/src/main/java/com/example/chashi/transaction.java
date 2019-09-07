package com.example.chashi;

public class transaction {

    private String productName;
    private String price;
    private String Description;
    private String amount;
    private String name;
    private String PhoneNo;
    private String village;
    private String upozila;
    private String district;
    private String division;

    public transaction(String productName, String price, String description, String amount, String name, String phoneNo, String village, String upozila, String district, String division) {
        this.productName = productName;
        this.price = price;
        Description = description;
        this.amount = amount;
        this.name = name;
        PhoneNo = phoneNo;
        this.village = village;
        this.upozila = upozila;
        this.district = district;
        this.division = division;
    }

    public transaction() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getUpozila() {
        return upozila;
    }

    public void setUpozila(String upozila) {
        this.upozila = upozila;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}

package com.example.chashi;

public class ProfileClass {
    String person_name, person_phon_no,orders;
    ProfileClass(){

    }
    ProfileClass(String person_name, String person_phon_no,String orders){
        this.person_name = person_name;
        this.person_phon_no = person_phon_no;
        this.orders = orders;
    }

    public String getPerson_name() {
        return person_name;
    }

    public String getPerson_phon_no() {
        return person_phon_no;
    }

    public String getOrders() {
        return orders;
    }
}

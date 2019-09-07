package com.example.chashi;

import java.util.Comparator;

public class MyPair implements Comparable {
    private String str;
    private int val = 0;

    public MyPair(String str, int val) {
        this.str = str;
        this.val = val;
    }


    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }


    @Override
    public int compareTo(Object o) {
        int j = ((MyPair) o).getVal();
        return this.val - j;
    }
}

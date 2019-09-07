package com.example.chashi;

public class Comments {
    private String uid;
    private String msg;
    private String name;
    private long time;

    public Comments(){

    }
    public Comments(String uid, String msg, String name, long time) {
        this.uid = uid;
        this.msg = msg;
        this.name = name;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

package com.example.chashi;

public class Comments {
    private String uid;
    private String msg;

    private long time;

    public Comments(){

    }
    public Comments(String uid, String msg, long time) {
        this.uid = uid;
        this.msg = msg;

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



    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

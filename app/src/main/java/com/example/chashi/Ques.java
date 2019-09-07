package com.example.chashi;

import java.util.HashMap;
import java.util.List;

public class Ques {
    private long time;
    private String ques;
    private String pushId;
    private String uid;
    private HashMap<String,Comments> comments;

    public Ques(){

    }

    public Ques(long time, String ques, String pushId, String uid, HashMap<String,Comments> comments) {
        this.time = time;
        this.ques = ques;
        this.pushId = pushId;
        this.uid = uid;
        this.comments = comments;
    }

    public Ques(long time, String ques, String pushId, String uid) {
        this.time = time;
        this.ques = ques;
        this.pushId = pushId;
        this.uid = uid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public HashMap<String,Comments> getComments() {
        return comments;
    }

    public void setComments(HashMap<String,Comments> comments) {
        this.comments = comments;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

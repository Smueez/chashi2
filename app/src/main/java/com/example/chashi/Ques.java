package com.example.chashi;

import java.util.List;

public class Ques {
    private long time;
    private String ques;
    private String pushId;
    private String uid;
    private List<Comments> comments;



    public Ques(long time, String ques, String pushId, String uid, List<Comments> comments) {
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

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
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

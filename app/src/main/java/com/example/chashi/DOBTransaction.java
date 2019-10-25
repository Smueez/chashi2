package com.example.chashi;

public class DOBTransaction {
    private String transId;
    private String time;
    private String accessToken;

    DOBTransaction(String transId, String time, String accessToken) {
        this.transId = transId;
        this.time = time;
        this.accessToken=accessToken;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

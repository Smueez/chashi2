package com.example.chashi;

public class DOBTransaction {
    private String transId;
    private String time;
    private String accessToken;
    private String errorCode;
    private String errorMessage;
    private boolean error = false;

    DOBTransaction(String transId, String time, String accessToken) {
        this.transId = transId;
        this.time = time;
        this.accessToken = accessToken;
    }

    DOBTransaction(boolean error, String errorCode, String errorMessage, String transId, String time) {
        this.error = true;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.transId = transId;
        this.time=time;
    }

    DOBTransaction(boolean error){
        this.error=true;
        errorMessage="UNKNOWN";
        errorCode="-1";
        this.transId = "N/A";
        this.time="N/A";
    }


    public boolean hasError() {
        return error;
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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

package com.example.chashi;

import android.os.AsyncTask;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CallOtpSendAPI extends AsyncTask<String, String, DOBTransaction> {

    private String phnNo;
    private String amount;
    private OnOTPSent onOTPSent;

    public CallOtpSendAPI(String phnNo, String amount, OnOTPSent onOTPSent) {

        this.phnNo = phnNo;
        this.onOTPSent = onOTPSent;
        //set context variables if required
    }


    @Override
    protected void onPostExecute(DOBTransaction s) {
        super.onPostExecute(s);
        onOTPSent.onTaskCompleted(s);
        //   Toast.makeText(context, res1,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    //0= url
    //1=data
    @Override
    protected DOBTransaction doInBackground(String... params) {


        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "client_id=fJYbGUfuJIGTNrXX1mmYvV2tN5HDc1Xo&client_secret=6UWFmdSnGRMkASWp&grant_type=client_credentials");
            Request request = new Request.Builder()
                    .url("https://apigw.grameenphone.com:9001/oauth/v1/token")
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", "PostmanRuntime/7.18.0")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "da1f89f5-7fac-45bd-973e-6f5c9a3df8b7,b298f727-4193-487c-9d1a-13d42d1f2b8d")
                    .addHeader("Host", "apigw.grameenphone.com:9001")
                    .addHeader("Accept-Encoding", "gzip, deflate")
                    .addHeader("Content-Length", "103")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = client.newCall(request).execute();
            String res1 = response.body().string();
            // Log.d("hi22-res1", res1);


            OkHttpClient client2 = new OkHttpClient();
            JSONObject userObject = new JSONObject(res1);
            String acToken = userObject.getString("accessToken");

            MediaType mediaType2 = MediaType.parse("application/json");
            RequestBody body2 = RequestBody.create(mediaType2, "{ \"sourceId\":\"AGWKuetG\", \"idType\":\"MSISDN\", \"amount\":\"" + amount + "\", \"priceCode\":\"PPU00021805630191022841\", \"serviceId\":\"PPU00021805630\", \"description\":\"pay first\" }");
            Request request2 = new Request.Builder()
                    .url("https://apigw.grameenphone.com:9001/payments/v2/customers/" + phnNo + "/pushotp")
                    .post(body2)
                    .addHeader("Accept-encoding", "application/gzip")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + acToken)
                    .addHeader("User-Agent", "PostmanRuntime/7.18.0")
                    .addHeader("Accept", "*/*")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "e0b2ebef-a9c5-476c-b237-b4cf5bf7454a,b77188f9-9df5-48e8-8ede-aa98e0d2a2f7")
                    .addHeader("Host", "apigw.grameenphone.com:9001")
                    .addHeader("Content-Length", "154")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response2 = client2.newCall(request2).execute();
            String res2 = response2.body().string();
            JSONObject userObject2 = new JSONObject(res2);
            if (userObject2.has("code")) {
                String code = userObject.getString("code");
                String message = userObject.getString("message");
                String transactionId = userObject.getString("transactionId");
                String time = userObject.getJSONObject("accessInfo").getString("timestamp");
                return new DOBTransaction(true, code, message, transactionId, time);
            } else {
                String time = userObject2.getJSONObject("accessInfo").getString("timestamp");
                String OTPtranId = userObject2.getJSONObject("data").getString("otpTrasactionId");
                //   Log.d("hi22-s2s3", s2+" "+s3);
                return new DOBTransaction(OTPtranId, time, acToken); //s3=transaction, s2=time, s=bearer
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new DOBTransaction(true);
    }
}

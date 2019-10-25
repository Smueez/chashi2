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


public class CallChargeAPI extends AsyncTask<String, String, DOBTransaction> {

    private String phnNo;
    private String otp;
    private String accessToken;
    private String otpTranId;
    private OnChargeDone onChargeDone;

    public CallChargeAPI(String phnNo, String otp, String accessToken, String otpTranId, OnChargeDone onChargeDone) {

        this.phnNo = phnNo;
        this.onChargeDone = onChargeDone;
        this.otp = otp;
        this.accessToken = accessToken;
        this.otpTranId = otpTranId;
        //set context variables if required
    }


    @Override
    protected void onPostExecute(DOBTransaction s) {
        super.onPostExecute(s);
        onChargeDone.onChargeDone(s);
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

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{ \"sourceId\": \"AGWKuetG\", \"idType\": \"MSISDN\",\"serviceId\": \"PPU00021805630\", \"transactionPin\": \"" + otp + "\", \"otpTransactionId\": \""+otpTranId+"\", \"category\": \"app\", \"description\": \"pay first\" }");
            Request request = new Request.Builder()
                    .url("https://apigw.grameenphone.com:9001/payments/v2/customers/" + phnNo + "/chargeotp")
                    .post(body)
                    .addHeader("Accept-encoding", "application/gzip")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("User-Agent", "PostmanRuntime/7.18.0")
                    .addHeader("Accept", "*/*")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "9e5e85ea-225e-4842-88f7-5efebf547e4a,6e6009d9-f3a7-49ee-b8fb-a2e0d511c365")
                    .addHeader("Host", "apigw.grameenphone.com:9001")
                    .addHeader("Content-Length", "198")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            JSONObject userObject = new JSONObject(res);
            if (userObject.has("code")) {
                String code = userObject.getString("code");
                String message = userObject.getString("message");
                String transactionId = userObject.getString("transactionId");
                String time = userObject.getJSONObject("accessInfo").getString("timestamp");
                return new DOBTransaction(true, code, message, transactionId, time);
            } else {
                String time = userObject.getJSONObject("accessInfo").getString("timestamp");
                String tranid = userObject.getJSONObject("data").getString("transactionId");
                return new DOBTransaction(tranid, time, accessToken);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new DOBTransaction(true);
    }
}

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

            Request request = new Request.Builder()
                    .url("https://developer.bdapps.com/caas/direct/debit")
                    .post(null)
                    .addHeader("externalTrxId", "234444")
                    .addHeader("amount", "5")
                    .addHeader("applicationId", "APP_020950")
                    .addHeader("password", "3a8821bf20e300e8cda37dcc9e68bcf5")
                    .addHeader("subscriberId", "tel: 8801620601009")
                    .addHeader("paymentInstrumentName", "Mobile Account")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("accountId", "8801620601009")
                    .addHeader("currency", "BDT")
                    .addHeader("User-Agent", "PostmanRuntime/7.20.1")
                    .addHeader("Accept", "*/*")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "efadc0b0-caab-44d0-8461-fc32b4bbad3c,855e0da3-6929-4276-926b-75ca729ffe71")
                    .addHeader("Host", "developer.bdapps.com")
                    .addHeader("Accept-Encoding", "gzip, deflate")
                    .addHeader("Content-Length", "0")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            String res = response.body().string();
            JSONObject userObject = new JSONObject(res);
            if (userObject.has("statusCode")) {
                String code = userObject.getString("statusCode");
                String message = userObject.getString("statusDetail");
                String transactionId = userObject.getString("internalTrxId");
                String time = userObject.getString("timeStamp");
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

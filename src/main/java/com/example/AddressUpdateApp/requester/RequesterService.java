package com.example.AddressUpdateApp.requester;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.UUID;

@Service
public class RequesterService {

    public void createRequester(String uid) {
    }

    public String generateOtp(String uid) {
        // Generate Transaction ID
        String txnId = UUID.randomUUID().toString();
        System.out.println("Printing txnId: " + txnId);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("txnId", txnId);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonResponse = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("https://stage1.uidai.gov.in/onlineekyc/getOtp/");
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);
            String json = EntityUtils.toString(response.getEntity());
            jsonResponse = new JSONObject(json);
            System.out.println(json);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String res = "Invalid data";
        try {
            if(jsonResponse.get("status").toString().equalsIgnoreCase("Y")) {
                res = "OTP Generation Successful";
            }
        }
        catch(JSONException je) {
            je.printStackTrace();
        }
        return res;
    }

    public String verifyOtp(String uid, String txnId, String otp) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("txnId", txnId);
            jsonObject.put("otp", otp);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonResponse = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("https://stage1.uidai.gov.in/onlineekyc/getAuth/");
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);
            String json = EntityUtils.toString(response.getEntity());
            jsonResponse = new JSONObject(json);
            System.out.println(json);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String res = "Invalid data";
        try {
            if(jsonResponse.get("status").toString().equalsIgnoreCase("y")) {
                res = "Authentication Successful";
            }
        }
        catch(JSONException je) {
            je.printStackTrace();
        }
        return res;
    }
}

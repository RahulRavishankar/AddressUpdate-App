package com.example.AddressUpdateApp.introducer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class IntroducerService {

    private final IntroducerRepository introducerRepository;

    @Autowired
    public IntroducerService(IntroducerRepository introducerRepository) {
        this.introducerRepository = introducerRepository;
    }

    public void addIntroducer(Introducer introducer) {

        introducer.setConsentProvided(Consent.AWAITING_RESPONSE);
        Optional<Introducer> introducerByUid = introducerRepository.findIntroducerByUid(introducer.getUid());
        System.out.println("Before inserting: " + introducer);
        if(!introducerByUid.isPresent()) {
            introducerRepository.save(introducer);
        }
        System.out.println(introducer);
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

    public void deleteIntroducer(String uid) {
        boolean exists = introducerRepository.existsById(uid);
        if(exists) {
           introducerRepository.deleteById(uid);
        }
        else {
            System.out.println("Could not delete user with uid: "+ uid + ". User does not exist");
        }
    }

    @Transactional
    public void updateIntroducer(String uid, String txnId) {
        boolean exists = introducerRepository.existsById(uid);
        if(exists) {
            Optional<Introducer> introducerByUid = introducerRepository.findById(uid); // or findIntroducerByUid
            if(introducerByUid.get().getTxnId() == null) {
                introducerByUid.get().setTxnId(txnId);
            }
        }
    }

    public String fetchAddress(String uid, String txnId, String otp) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", uid);
            jsonObject.put("txnId", txnId);
            jsonObject.put("otp", otp);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        JSONObject jsonRes = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("https://stage1.uidai.gov.in/onlineekyc/getEkyc/");
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);
            String json = EntityUtils.toString(response.getEntity());
            jsonRes = new JSONObject(json);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(jsonRes);
        return jsonRes.toString();
    }

    public void updateConsent(String uid, String requesterUid, Consent consent) {
        boolean exists = introducerRepository.existsById(uid);
        if(exists) {
            Optional<Introducer> introducerByUid = introducerRepository.findIntroducerByUidAndRequesterUid(uid, requesterUid); // or findIntroducerByUid
            if(introducerByUid.get().getConsentProvided() != consent) {
                System.out.println("Before updating: "+ introducerByUid.get());
                introducerByUid.get().setConsentProvided(consent);
            }
        }

        // once the consent is given, notify the requester
    }
}

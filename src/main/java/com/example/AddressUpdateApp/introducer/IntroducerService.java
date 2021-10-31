package com.example.AddressUpdateApp.introducer;

import com.example.AddressUpdateApp.utils.Consent;
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
import java.util.*;

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

    public String fetchAddress(String uid, String txnId, String otp, String requesterUid) {

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

        // decode the address

        String decodedAddress = "";
        //////Store the address in the introducer database
        this.updateAddress(uid, requesterUid, decodedAddress);

        jsonObject = new JSONObject();
        try {
            jsonObject.put("address", decodedAddress);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        //////Store the address in the requester database
        httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("https://locahost:8080/requester/updateAddress/"+requesterUid);
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);
//            String json = EntityUtils.toString(response.getEntity());
//            jsonRes = new JSONObject(json);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /////////////////////////////////

        System.out.println(jsonRes);
        return jsonRes.toString();
    }

    @Transactional
    private void updateAddress(String uid, String requesterUid, String address) {
        boolean exists = introducerRepository.existsById(uid);
        if(exists) {
            Optional<Introducer> introducerByUid = introducerRepository.findIntroducerByUidAndRequesterUid(uid, requesterUid); // or findIntroducerByUid
            if(introducerByUid.get().getAddress() != address) {
                introducerByUid.get().setAddress(address);
            }
        }
    }

    @Transactional
    public void updateConsent(String uid, String requesterUid, Consent consent) {
        boolean exists = introducerRepository.existsById(uid);
        if(exists) {
            Optional<Introducer> introducerByUid = introducerRepository.findIntroducerByUidAndRequesterUid(uid, requesterUid); // or findIntroducerByUid
            if(introducerByUid.get().getConsentProvided() != consent) {
                introducerByUid.get().setConsentProvided(consent);
            }
        }

        // Notify the requester
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("http://localhost:8080/requester/updateConsent/" +
                    requesterUid + "/" + uid + "/" + consent);
            HttpResponse response = httpClient.execute(request);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, String> getAllRequesters(String uid) {
        Optional<Introducer> requesters = introducerRepository.findAllById(uid);
        HashMap<String, String> map = new HashMap<>();

        List<String> res = new ArrayList<String>();
        requesters.ifPresent(r -> {
            res.add(r.getRequesterUid()+r.getConsentProvided());
            map.put(r.getRequesterUid(), r.getRequesterUid());
        });


//        return res.toString();
        return map;
    }
}

package com.example.AddressUpdateApp.requester;

import com.example.AddressUpdateApp.introducer.Introducer;
import com.example.AddressUpdateApp.utils.Consent;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.transaction.Transactional;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
@Service
@Configuration
@PropertySource("classpath:application.properties")
public class RequesterService {

    private final RequesterRepository requesterRepository;

    @Autowired
    public RequesterService(RequesterRepository requesterRepository) {
        this.requesterRepository = requesterRepository;
    }

    public void addRequester(Requester requester) {
        Optional<Requester> requesterByUid = requesterRepository.findRequesterByUid(requester.getUid());
        if(!requesterByUid.isPresent()) {
            requesterRepository.save(requester);
        }
        System.out.println(requester);
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
        JSONObject jsonres = new JSONObject();
        jsonres = jsonObject;
        try {
            if(jsonResponse.get("status").toString().equalsIgnoreCase("Y")) {
                res = "OTP Generation Successful";
                System.out.println(res);
                jsonres.put("success", "Y");
                
            }
        }
        catch(JSONException je) {
            je.printStackTrace();
        }
        System.out.println("returning"+jsonres.toString());
        return jsonres.toString();
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

    public void deleteRequester(String uid) {
        boolean exists = requesterRepository.existsById(uid);
        if(exists) {
           requesterRepository.deleteById(uid);
        }
        else {
            System.out.println("Could not delete user with uid: "+ uid + ". User does not exist");
        }
    }

    @Transactional
    public void updateRequester(String uid, String txnId) {
        boolean exists = requesterRepository.existsById(uid);
        if(exists) {
            Optional<Requester> requesterByUid = requesterRepository.findById(uid); // or findRequesterByUid
            if(requesterByUid.get().getTxnId() == null) {
                requesterByUid.get().setTxnId(txnId);
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
        String jsonstr = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String data = null;
        try {
            HttpPost request = new HttpPost("https://stage1.uidai.gov.in/onlineekyc/getEkyc/");
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

            HttpResponse response = httpClient.execute(request);
            InputStream json = response.getEntity().getContent();
            jsonstr = IOUtils.toString(json);
            JSONObject jsonBody = new JSONObject(jsonstr.substring(jsonstr.indexOf("{"), jsonstr.lastIndexOf("}") + 1));
            JSONObject ekycstring = jsonBody.getJSONObject("eKycString");
            String rawdata = ekycstring.toString();
            data = rawdata.substring(rawdata.indexOf("{"), rawdata.lastIndexOf("}") + 1);
//            jsonRes = new JSONObject(jsonstr);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        DocumentBuilder builder;
        Element rootElement ;
        try
        {
            builder = factory.newDocumentBuilder();
 
            // Use String reader
            Document document = builder.parse( new InputSource(
                    new StringReader( data ) ) );
            rootElement = document.getDocumentElement();
//            TransformerFactory tranFactory = TransformerFactory.newInstance();
//            Transformer aTransformer = tranFactory.newTransformer();
//            Source src = new DOMSource( document );
//            Result dest = new StreamResult( new File( "xmlFileName.xml" ) );
//            aTransformer.transform( src, dest );
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //rootelement ?
        System.out.println(data);
        return data;
    }

    public String requestConsent(String uid, String introducerUid, String toEmail) {
        // Add Introducer to the database
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", introducerUid);
            jsonObject.put("requesterUid", uid);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("http://localhost:8080/introducer/addIntroducer");
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

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

        ///Send email/////////
//        String toEmail = "rahul.k.ravishankar@gmail.com";
        jsonObject = new JSONObject();
        try {
            jsonObject.put("toEmail", toEmail);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("http://localhost:8080/emailApi/sendEmail");
            StringEntity params = new StringEntity(jsonObject.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);

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
        ////////////////////////


        // What happens if an invalid introducerUid is entered?
        return "Request sent";
    }

    @Value("${maps.api-key}") String mapsAPIKey;
    public String verifyAddress(String uid, String src, String dst) {

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = null;
        double distance = 0.0;
        try {
            src = src.replace(' ', '+');
            dst = dst.replace(' ', '+');

//            System.out.println(src);
//            System.out.println(dst);
//            System.out.println(mapsAPIKey);
            
//            CloseableHttpClient client = HttpClientBuilder.create().build();
//            		Request request = new Request.Builder()
//            		  .url("https://maps.googleapis.com/maps/api/distancematrix/json?origins=Washington%2C%20DC&destinations=New%20York%20City%2C%20NY&units=imperial&key=YOUR_API_KEY")
//            		  .method("GET", null)
//            		  .build();
//            		Response response = client.newCall(request).execute();
            HttpGet request = new HttpGet("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                    src + "&destinations=" + dst+"&key="+mapsAPIKey);

            response = httpClient.execute(request);
//            response.
            InputStream body = response.getEntity().getContent();
            String bod = IOUtils.toString(body);
//            System.out.println(bod);
            JSONObject jsonBody = new JSONObject(bod.substring(bod.indexOf("{"), bod.lastIndexOf("}") + 1));
            JSONArray jsonarr = jsonBody.getJSONArray("rows");
//            System.out.println(jsonarr.toString());
            JSONObject jele = jsonarr.getJSONObject(0);
//            System.out.println(jele.toString());
            JSONArray jarr = jele.getJSONArray("elements");
//            System.out.println(jarr.toString());
            JSONObject dist = jarr.getJSONObject(0);
            JSONObject finaldist = dist.getJSONObject("distance");
            distance = finaldist.getDouble("value");
//            System.out.println(""+distance);
            
//            double distance = (double) jsonBody.get("rows").get("elements").get("distance").get("value");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String res = "N";
        if(distance <= 20.000000000)
        	res = "Y";
        return res;
    }

    @Transactional
    public String updateNewAddress(String uid, String newAddress) {
        boolean exists = requesterRepository.existsById(uid);
        if(exists) {
            Optional<Requester> requesterByUid = requesterRepository.findById(uid);
            if(requesterByUid.get().getNewAddress() == null) {
                requesterByUid.get().setNewAddress(newAddress);
            }
        }

        return "Update Successful";
    }

    @Transactional
    public void updateAddress_(String uid, String address) {
        boolean exists = requesterRepository.existsById(uid);
        if(exists) {
            Optional<Requester> introducerByUid = requesterRepository.findRequesterByUid(uid);
            if(introducerByUid.get().getAddress() != address) {
                introducerByUid.get().setAddress(address);
            }
        }
    }

    @Transactional
    public void updateConsent(String uid, String introducerUid, Consent consent) {
        boolean exists = requesterRepository.existsById(uid);
        if(exists) {
            Optional<Introducer> introducerByUid = requesterRepository.findRequesterByUidAndIntroducerUid(uid, introducerUid); // or findIntroducerByUid
            if(introducerByUid.get().getConsentProvided() != consent) {
                introducerByUid.get().setConsentProvided(consent);
            }
        }
    }
}

package com.example.AddressUpdateApp.requester;

import com.example.AddressUpdateApp.authapi.HelperClass;
import com.example.AddressUpdateApp.authapi.OTPAuth;
import com.example.AddressUpdateApp.otpapi.OtpAPIService;
import com.example.AddressUpdateApp.otpapi.model.OtpRes;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class RequesterService {

    // implement the actual functions
    /*
    * Input UID and create a requester
    * Generate OTP using OTP API
    * Validate OTP using Auth API
    * */

    public void createRequester(String uid) {

    }

    public String generateOtp(String uid) {
        // Generate Transaction ID
        String txnId = UUID.randomUUID().toString();
        System.out.println("Printing txnId: " + txnId);

        // Create OTP API Service object
        OtpAPIService otpAPIService = new OtpAPIService();
        try {
            otpAPIService.readProperties();
        }
        catch(IOException e) {
            // Handle exception
        }

        // Generate OTP
        OtpRes otpRes = null;
        try {
            otpRes = otpAPIService.getOtpRes(uid, txnId);
        }
        catch(Exception e) {
            // Handle exception
        }

//        System.out.println("Result : " + otpRes.getRet().value() + ", err: " + otpRes.getErr());
        return (otpRes.getRet().value().equals("y")?"OTP Generation Successful":otpRes.getErr());
    }

    public String verifyOtp(String uid, String txn, String otpInRRequest) {

        OTPAuth otpAuth = new OTPAuth();
        String myAuthRes = null;

        try {
            otpAuth.readProperties();
            HelperClass helperClass = new HelperClass(otpAuth.configProp);

//            String uid = args[0];
//            String txn = args[1];
//            String otpInRRequest = args[2];
            System.out.println("UID: "+uid+" TxnId: "+txn+" OTP :"+otpInRRequest);
            auth_2_0.Auth auth = otpAuth.createResidentAuth(uid, txn, otpInRRequest, new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm:ss").format(new Date()), "");
            auth_2_0.AuthResponseDetailsV2 data = helperClass.getAuthResponseDetailsV2(auth);

            // Get Auth Response
            auth_2_0.AuthRes authRes = data.getAuthRes();
            myAuthRes = authRes.getRet().toString();
            System.out.println("Auth Response: " + authRes.getRet().toString());
            if (authRes.getErr() != null)
                System.out.println("ErrorCode: "+authRes.getErr());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myAuthRes;
    }
}

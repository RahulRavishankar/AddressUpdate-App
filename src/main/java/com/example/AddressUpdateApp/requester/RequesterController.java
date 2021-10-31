package com.example.AddressUpdateApp.requester;

import com.example.AddressUpdateApp.utils.Consent;
import com.example.AddressUpdateApp.utils.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AddressUpdateApp.utils.VerifyAddr;
import com.example.AddressUpdateApp.utils.NewAddress;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path="requester")
public class RequesterController {

    private final RequesterService requesterService;

    // Dependency Injection
    @Autowired
    public RequesterController(RequesterService requesterService) {
        this.requesterService = requesterService;
    }



    @GetMapping(path="generateOtp/{uid}")
    public String generateOtp(@PathVariable("uid") String uid) {
        return requesterService.generateOtp(uid);
    }

    @GetMapping(path="verifyOtp/{uid}/{txn}/{otp}")
    public String verifyOtp(@PathVariable("uid") String uid, @PathVariable("txn") String txnId, @PathVariable("otp") String otp) {
        return requesterService.verifyOtp(uid, txnId, otp);
    }

    @GetMapping(path="fetchAddress/{uid}/{txn}/{otp}")
    public String fetchAddress(@PathVariable("uid") String uid, @PathVariable("txn") String txnId, @PathVariable("otp") String otp) {
        return requesterService.fetchAddress(uid, txnId, otp);
    }

    @PostMapping(path="addRequester", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void addRequester(@RequestBody Requester requester) {
        requesterService.addRequester(requester);
    }

    @DeleteMapping(path="deleteRequester/{uid}")
    public void deleteRequester(@PathVariable("uid") String uid) {
        requesterService.deleteRequester(uid);
    }

    @PutMapping(path="updateTxnId/{uid}/{txnId}")
    public void updateTxnId(@PathVariable("uid") String uid, @PathVariable("txnId") String txnId) {
        requesterService.updateRequester(uid, txnId);
    }

    @PostMapping(path="requestConsent/{uid}/{introducerUid}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String requestConsent(@PathVariable("uid") String uid, @PathVariable("introducerUid") String introducerUid,
                                 @RequestBody Email toEmail) {
        return requesterService.requestConsent(uid, introducerUid, toEmail.getEmail());
    }


    @PostMapping(path="verifyAddress/{uid}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String verifyAddress(@PathVariable("uid") String uid, @RequestBody VerifyAddr verifyAddr) {
        return requesterService.verifyAddress(uid, verifyAddr.getSrc(), verifyAddr.getDst());
    }

    @PostMapping(path="updateAddressFinal/{uid}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String updateAddress(@PathVariable("uid") String uid, @RequestBody NewAddress newAddr) {
        return requesterService.updateNewAddress(uid, newAddr.getAddress());
    }

    @PostMapping(path="updateAddress/{uid}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void updateAddress_(@PathVariable("uid") String uid, @RequestBody NewAddress newAddr) {
        requesterService.updateAddress_(uid, newAddr.getAddress());
    }

    @GetMapping(path="updateConsent/{uid}/{introducerUid}/{consent}")
    public void provideConsent(@PathVariable("uid") String uid, @PathVariable("introducerUid") String introducerUid,
                               @PathVariable("consent") Consent consent) {
        requesterService.updateConsent(uid, introducerUid, consent);
    }
}
package com.example.AddressUpdateApp.requester;

import com.example.AddressUpdateApp.utils.VerifyAddr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping(path="addRequester")
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

    @GetMapping(path="requestConsent/{uid}/{introducerUid}")
    public String requestConsent(@PathVariable("uid") String uid, @PathVariable("introducerUid") String introducerUid) {
        return requesterService.requestConsent(uid, introducerUid);
    }

    @PostMapping(path="verifyAddress/{uid}")
    public String verifyAddress(@PathVariable("uid") String uid, @RequestBody VerifyAddr verifyAddr) {
        return requesterService.verifyAddress(uid, verifyAddr.getSrc(), verifyAddr.getDst());
    }

    @PostMapping(path="updateAddressFinal/{uid}")
    public String updateAddress(@PathVariable("uid") String uid, @RequestBody NewAddress newAddr) {
        return requesterService.updateNewAddress(uid, newAddr.getAddress());
    }
}
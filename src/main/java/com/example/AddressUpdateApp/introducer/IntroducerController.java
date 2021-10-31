package com.example.AddressUpdateApp.introducer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(path="introducer")
public class IntroducerController {

    private final IntroducerService introducerService;

    // Dependency Injection
    @Autowired
    public IntroducerController(IntroducerService introducerService) {
        this.introducerService = introducerService;
    }



    @GetMapping(path="generateOtp/{uid}")
    public String generateOtp(@PathVariable("uid") String uid) {
        return introducerService.generateOtp(uid);
    }

    @GetMapping(path="verifyOtp/{uid}/{txn}/{otp}")
    public String verifyOtp(@PathVariable("uid") String uid, @PathVariable("txn") String txnId, @PathVariable("otp") String otp) {
        return introducerService.verifyOtp(uid, txnId, otp);
    }

    @GetMapping(path="fetchAddress/{uid}/{txn}/{otp}")
    public String fetchAddress(@PathVariable("uid") String uid, @PathVariable("txn") String txnId, @PathVariable("otp") String otp) {
        return introducerService.fetchAddress(uid, txnId, otp);
    }

    @PostMapping(path="addIntroducer")
    public void addIntroducer(@RequestBody Introducer introducer) {
        introducerService.addIntroducer(introducer);
    }

    @DeleteMapping(path="deleteIntroducer/{uid}")
    public void deleteIntroducer(@PathVariable("uid") String uid) {
        introducerService.deleteIntroducer(uid);
    }

    @PutMapping(path="updateTxnId/{uid}/{txnId}")
    public void updateTxnId(@PathVariable("uid") String uid, @PathVariable("txnId") String txnId) {
        introducerService.updateIntroducer(uid, txnId);
    }

    @GetMapping(path="provideConsent/{uid}/{requesterUid}")
    public void provideConsent(@PathVariable("uid") String uid, @PathVariable("requesterUid") String requesterUid) {
        introducerService.updateConsent(uid, requesterUid, Consent.GIVEN);
    }

    @GetMapping(path="declineConsent/{uid}/{requesterUid}")
    public void declineConsent(@PathVariable("uid") String uid, @PathVariable("requesterUid") String requesterUid) {
        introducerService.updateConsent(uid, requesterUid, Consent.NOT_GIVEN);
    }

    @GetMapping(path="getAllRequesters/{uid}")
    public String getAllRequesters(@PathVariable("uid") String uid) {
        return introducerService.getAllRequesters(uid);
    }

}

package com.example.AddressUpdateApp.introducer;

import com.example.AddressUpdateApp.utils.Consent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @GetMapping(path="fetchAddress/{uid}/{txn}/{otp}/{requesterUid}")
    public String fetchAddress(@PathVariable("uid") String uid, @PathVariable("txn") String txnId, @PathVariable("otp") String otp,
                               @PathVariable("requesterUid") String requesterUid) {
        return introducerService.fetchAddress(uid, txnId, otp, requesterUid);
    }

    @PostMapping(path="addIntroducer", consumes = {MediaType.APPLICATION_JSON_VALUE})
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

    @PostMapping(path="getAllRequesters/{uid}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String getAllRequesters(@PathVariable("uid") String uid, @RequestBody Introducer introducer) {
        System.out.println("Heloooooooooo" + introducer.getUid());
        return introducerService.getAllRequesters(uid);
    }

}

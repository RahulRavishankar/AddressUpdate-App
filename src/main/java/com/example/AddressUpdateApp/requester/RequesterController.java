package com.example.AddressUpdateApp.requester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
//        requesterService.addRequester(uid);
        return requesterService.generateOtp(uid);
    }

    @GetMapping(path="verifyOtp/{uid}/{txn}/{otp}")
    public String verifyOtp(@PathVariable("uid") String uid, @PathVariable("txn") String txn, @PathVariable("otp") String otpInRRequest) {
        return requesterService.verifyOtp(uid, txn, otpInRRequest);
    }

    @PostMapping
    public void addRequester(@RequestBody Requester requester) {
        requesterService.addRequester(requester);
    }

    @DeleteMapping(path="{uid}")
    public void deleteStudent(@PathVariable("uid") String uid) {
        requesterService.deleteStudent(uid);
    }

    @PutMapping(path="{uid}/{txnId}")
    public void updateTxnId(@PathVariable("uid") String uid, @PathVariable("txnId") String txnId) {
        requesterService.updateRequester(uid, txnId);
    }
}

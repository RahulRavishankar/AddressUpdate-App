package com.example.AddressUpdateApp.requester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
//        requesterService.createRequester(uid);
        return requesterService.generateOtp(uid);
    }

    @GetMapping(path="verifyOtp/{uid}/{txn}/{otp}")
    public String verifyOtp(@PathVariable("uid") String uid, @PathVariable("txn") String txn, @PathVariable("otp") String otpInRRequest) {
        return requesterService.verifyOtp(uid, txn, otpInRRequest);
    }
}

package com.example.AddressUpdateApp.sendgrid;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AddressUpdateApp.sendgrid.MailService;



@RestController
@RequestMapping(value = "/api")
public class MailController {

	@Autowired
	MailService mailService;
	
	@PostMapping("/send-text")
	public String send() throws IOException {
		return mailService.sendTextEmail();
	}
	
	
//	@PostMapping("/send")
//	public String sendWithTemplate() throws IOException {
//		return mailService.send();
//	}

}
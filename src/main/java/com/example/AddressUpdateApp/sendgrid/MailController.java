package com.example.AddressUpdateApp.sendgrid;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.AddressUpdateApp.sendgrid.MailService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/emailApi")
public class MailController {

	private final MailService mailService;

	@Autowired
	public MailController(MailService mailService) {
		this.mailService = mailService;
	}

	@PostMapping("/sendEmail/{toEmail}")
	public String send(@PathVariable("toEmail") String toEmail) throws IOException {
		return mailService.sendTextEmail("aadhaar.address.update@gmail.com", toEmail, "[UIDAI] Address Update");
	}
	
	
//	@PostMapping("/send")
//	public String sendWithTemplate() throws IOException {
//		return mailService.send();
//	}

}
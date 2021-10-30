package com.example.AddressUpdateApp.sendgrid;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

@Service
@Configuration
@PropertySource("classpath:application.properties")
public class MailService {
//	private static final Logger logger = LoggerFactory.getLogger(MailService.class);
	
	@Value("${spring.sendgrid.api-key}") String sendGridAPIKey;
	public String sendTextEmail(String src, String dest, String subj) throws IOException {
		/*
		 * The sender email should be the same as we used to Create a Single Sender
		 * Verification
		 */
		Email from = new Email(src);
		String subject = subj;
		Email to = new Email(dest);
		Content content = new Content("text/plain", "Please provide consent to <RequesterUid> at http://localhost:8080/ . " +
				"Link expires in 24hrs");
		Mail mail = new Mail(from, subject, to, content);

		SendGrid sg = new SendGrid(sendGridAPIKey);
		Request request = new Request();
		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
//			logger.info(response.getBody());
			return response.getBody();
		} catch (IOException ex) {
			throw ex;
		}
	}
}
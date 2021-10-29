package com.example.AddressUpdateApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication  //(exclude = {DataSourceAutoConfiguration.class})
public class AddressUpdateAppApplication {

	public static void main(String[] args) {

		SpringApplication.run(AddressUpdateAppApplication.class, args);
		System.out.println("Hello, World");
	}

}

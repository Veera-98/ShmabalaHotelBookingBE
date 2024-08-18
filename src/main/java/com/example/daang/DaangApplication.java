package com.example.daang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class DaangApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaangApplication.class, args);
	}

}


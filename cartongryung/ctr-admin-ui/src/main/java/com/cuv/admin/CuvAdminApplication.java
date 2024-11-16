package com.cuv.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableScheduling
public class CuvAdminApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		SpringApplication.run(CuvAdminApplication.class, args);
	}

}

package com.middleware.apitv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.middleware.apitv.configuration.MongoConfig;

@SpringBootApplication
@Import(MongoConfig.class) 
public class ApitvApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApitvApplication.class, args);     
	}
}

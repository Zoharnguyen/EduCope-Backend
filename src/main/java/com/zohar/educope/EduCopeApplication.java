package com.zohar.educope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class EduCopeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduCopeApplication.class, args);
	}

}

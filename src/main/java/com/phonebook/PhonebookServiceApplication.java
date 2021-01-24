package com.phonebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = {"com.phonebook.*"})
@EnableSwagger2
public class PhonebookServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhonebookServiceApplication.class, args);
	}

}

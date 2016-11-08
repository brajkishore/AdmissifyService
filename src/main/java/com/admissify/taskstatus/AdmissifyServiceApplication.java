package com.admissify.taskstatus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.admissify.controllers","com.admissify.models"})
public class AdmissifyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdmissifyServiceApplication.class, args);
	}
}

package com.assignment.topia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.assignment.topia")
public class TopioApplication {

	public static void main(String[] args) {
		SpringApplication.run(TopioApplication.class, args);
	}

}

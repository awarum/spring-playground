package com.nwboxed.simplespring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarApplication {

	private static final Logger log = LoggerFactory.getLogger(CarApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CarApplication.class, args);
	}


}

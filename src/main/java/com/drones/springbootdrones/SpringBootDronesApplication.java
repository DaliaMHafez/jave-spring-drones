package com.drones.springbootdrones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootDronesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDronesApplication.class, args);
	}

}

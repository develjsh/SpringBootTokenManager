package com.example.springboottokenmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SpringBootTokenManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootTokenManagerApplication.class, args);
	}

}

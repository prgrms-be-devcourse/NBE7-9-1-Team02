package com.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Nbe791Team02Application {
	public static void main(String[] args) {
		SpringApplication.run(Nbe791Team02Application.class, args);
	}
}

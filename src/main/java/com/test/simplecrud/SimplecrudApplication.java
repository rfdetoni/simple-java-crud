package com.test.simplecrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class SimplecrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplecrudApplication.class, args);
	}

}

package com.example.avalancheLabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("com.example.avalancheLabs.repositories")
@EntityScan("com.example.avalancheLabs.models")
@SpringBootApplication
public class AvalancheLabsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AvalancheLabsApplication.class, args);
	}

}

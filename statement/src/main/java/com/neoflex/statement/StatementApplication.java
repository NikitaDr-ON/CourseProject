package com.neoflex.statement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.neoflex.statement.model")
public class StatementApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatementApplication.class, args);
	}

}

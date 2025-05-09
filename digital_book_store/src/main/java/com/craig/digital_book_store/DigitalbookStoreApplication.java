package com.craig.digital_book_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.craig.digital_book_store")
@EnableJpaRepositories("com.craig.digital_book_store.repository")
@EntityScan("com.craig.digital_book_store.model")
public class DigitalbookStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalbookStoreApplication.class, args);
	}

}

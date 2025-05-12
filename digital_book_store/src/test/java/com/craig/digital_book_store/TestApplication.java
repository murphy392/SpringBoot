package com.craig.digital_book_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.craig.digital_book_store.testmodel")
@EnableJpaRepositories("com.craig.digital_book_store.testrepo")
@ComponentScan({
    "com.craig.digital_book_store.testcontroller",
    "com.craig.digital_book_store.testservice",
    "com.craig.digital_book_store.testexceptions"
})
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

}

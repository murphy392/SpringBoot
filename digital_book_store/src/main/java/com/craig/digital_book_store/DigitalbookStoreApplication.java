package com.craig.digital_book_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class DigitalbookStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalbookStoreApplication.class, args);
	}

}

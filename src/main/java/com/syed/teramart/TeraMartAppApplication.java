package com.syed.teramart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TeraMartAppApplication {

	public static void main(String[] args) {
		System.out.println(System.getenv("DB_URL"));
		SpringApplication.run(TeraMartAppApplication.class, args);
	}

}

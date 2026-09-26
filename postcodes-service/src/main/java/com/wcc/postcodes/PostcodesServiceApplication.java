package com.wcc.postcodes;

import com.wcc.commons.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(GlobalExceptionHandler.class)
public class PostcodesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostcodesServiceApplication.class, args);
	}

}

package com.wcc.users;

import com.wcc.commons.exception.GlobalExceptionHandler;
import com.wcc.commons.exception.ProblemDetailAuthenticationEntryPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({GlobalExceptionHandler.class, ProblemDetailAuthenticationEntryPoint.class})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

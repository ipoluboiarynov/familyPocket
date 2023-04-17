package com.ivan4usa.fp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.ivan4usa.fp.*"})
public class FpApplication {
    public static void main(String[] args) {
        SpringApplication.run(FpApplication.class, args);
    }
}

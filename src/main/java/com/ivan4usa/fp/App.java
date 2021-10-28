package com.ivan4usa.fp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

/**
 * Server for Family Pocket App
 *
 */
@Controller
@SpringBootApplication
public class App {

    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
        System.out.println( "The server started ...");
    }
}

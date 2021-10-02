package com.ivan4usa.fp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {

    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
        System.out.println( "The server started ...");
    }
}

package com.izzyacademy.sales.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.izzyacademy.sales"})
public class Application {

    /**
     * Entry Point for the Application
     *
     * @param args Arguments for the Application
     */
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}

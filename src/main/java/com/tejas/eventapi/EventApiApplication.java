package com.tejas.eventapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the application.
 *
 * @SpringBootApplication is actually three annotations combined into one:
 *   - @Configuration       -> this class can define Spring beans
 *   - @EnableAutoConfiguration -> Spring Boot guesses sensible config based
 *                                 on the dependencies on the classpath
 *                                 (e.g. seeing MySQL + JPA on the classpath,
 *                                 it auto-configures a DataSource for us)
 *   - @ComponentScan       -> Spring scans this package and sub-packages
 *                             for classes annotated with @Component,
 *                             @Service, @Repository, @RestController, etc.,
 *                             and registers them as "beans" it manages
 *
 * This is the "auto-configuration" you watched video #9 about — it's why
 * we didn't have to manually wire up a database connection object anywhere.
 */
@SpringBootApplication
public class EventApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventApiApplication.class, args);
    }
}

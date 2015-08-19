package org.tiltedwindmills.twitter.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Primary class used to initiate the Spring Boot application.
 *
 * @author John Daniel
 */
// CHECKSTYLE:OFF
// jd - hiding checks for utility class private constructor and final class, as neither are compatible with Spring Boot.
@SpringBootApplication
public class TwitterUtilitiesApplication {
// CHECKSTYLE:ON

    /**
     * The main method.  Bootstraps the application and loads the Spring context.
     *
     * @param args any command line arguments
     */
    // CHECKSTYLE:OFF
    // jd - allowing main method, because.. you know.. kinda necessary here.
    public static void main(final String[] args) {
    // CHECKSTYLE:ON

        SpringApplication.run(TwitterUtilitiesApplication.class, args);
    }
}

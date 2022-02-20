package it.petrovich.rssprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RssProcessorApplication {

    public static void main(final String[] args) {
        SpringApplication.run(RssProcessorApplication.class, args);
    }

}

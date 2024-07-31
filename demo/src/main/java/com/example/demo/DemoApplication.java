package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.logging.Logger;

@SpringBootApplication
@Slf4j
//@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@EnableAutoConfiguration
public class DemoApplication {

    Logger logger = Logger.getLogger(String.valueOf(DemoApplication.class));

    public static void main(String[] args) {
        System.out.println("Start apps.");
        SpringApplication.run(DemoApplication.class, args);
    }

    public void log() {
        logger.info("Start apps");
    }

}

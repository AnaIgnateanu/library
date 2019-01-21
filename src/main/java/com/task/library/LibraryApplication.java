package com.task.library;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableBatchProcessing
public class LibraryApplication {
    @Autowired
    public DataSource dataSource;

    public static void main(String[] args) {
        //DatabaseThreadContext.setCurrentDatabase(Database.SECONDARY);
        SpringApplication.run(LibraryApplication.class, args);
    }

   // @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    //@Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
            String books = restTemplate.getForObject(
                    "http://localhost:8081/books", String.class);
            System.out.println(books);
        };
    }
}

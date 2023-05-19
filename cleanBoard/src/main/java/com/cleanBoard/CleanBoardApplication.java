package com.cleanBoard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CleanBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(CleanBoardApplication.class, args);
    }

}

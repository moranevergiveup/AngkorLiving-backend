package com.mora.angkorleving_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.mora.angkorleving_backend.Repository") // repository package
@EntityScan("com.mora.angkorleving_backend.entity")
public class AngkorLevingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AngkorLevingBackendApplication.class, args);
    }

}

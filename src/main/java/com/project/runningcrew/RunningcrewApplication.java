package com.project.runningcrew;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RunningcrewApplication {

    public static void main(String[] args) {
        SpringApplication.run(RunningcrewApplication.class, args);
    }

}

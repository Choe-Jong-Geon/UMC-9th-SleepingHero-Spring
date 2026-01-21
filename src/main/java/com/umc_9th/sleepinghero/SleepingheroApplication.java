package com.umc_9th.sleepinghero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SleepingheroApplication {

    public static void main(String[] args) {
        SpringApplication.run(SleepingheroApplication.class, args);
    }

}

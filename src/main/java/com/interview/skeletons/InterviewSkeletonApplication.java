package com.interview.skeletons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableCaching
public class InterviewSkeletonApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewSkeletonApplication.class, args);
    }

}

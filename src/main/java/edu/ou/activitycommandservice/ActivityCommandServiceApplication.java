package edu.ou.activitycommandservice;

import edu.ou.coreservice.annotation.BaseCommandAnnotation;
import org.springframework.boot.SpringApplication;

@BaseCommandAnnotation
public class ActivityCommandServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActivityCommandServiceApplication.class, args);
    }

}

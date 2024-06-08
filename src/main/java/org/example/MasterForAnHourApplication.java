package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "org.example")
public class MasterForAnHourApplication {
    public static void main(String[] args) {
        SpringApplication.run(MasterForAnHourApplication.class, args);
    }
}

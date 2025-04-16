package com.rrtyui.filestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FilestorageApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FilestorageApplication.class, args);
    }

}

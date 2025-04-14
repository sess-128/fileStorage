package com.rrtyui.filestorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FilestorageApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FilestorageApplication.class, args);
        System.out.println(context.getBeanDefinitionCount());
    }

}

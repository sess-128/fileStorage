package com.rrtyui.filestorage.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("File storage API")
                                .description("API файлового хранилища")
                                .version("1.0")
                                .contact(new Contact()
                                        .name("Andrew")
                                        .url("https://t.me/hesaro_kan"))
                )
                .servers(
                        List.of(
                                new Server()
                                        .url("http://localhost:8080")
                        )
                );
    }
}

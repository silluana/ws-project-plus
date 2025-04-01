package com.client.ws.projectplus.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RasmooPlus")
                        .version("0.0.1")
                        .description("API para atender o cliente Rasmoo Plus")
                        .contact(new Contact()
                                .name("Siluana Oggioni")
                                .email("siluanaoggionidev@gmail.com"))
                );
    }
}
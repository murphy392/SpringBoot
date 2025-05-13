package com.craig.digital_book_store.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
               .components(new Components()
                .addSecuritySchemes("basicscheme", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("basic")))
                .info(new Info().title("Book Store API").version("1.0.0"));
    }
}

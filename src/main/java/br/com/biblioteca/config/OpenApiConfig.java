package br.com.biblioteca.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bibliotecaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Biblioteca API")
                        .description("Microservico REST para gerenciamento de livros e categorias")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Grupo Biblioteca")
                                .email("grupo@example.com")));
    }
}

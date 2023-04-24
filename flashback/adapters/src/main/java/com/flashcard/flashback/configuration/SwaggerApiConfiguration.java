package com.flashcard.flashback.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class SwaggerApiConfiguration {

    @Value("/api/documentation/ui")
    private String swaggerPath;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addRedirectViewController(swaggerPath, "/swagger-ui.html");
            }
        };
    }

    @Bean
    public GroupedOpenApi flashcardsApi() {
        return GroupedOpenApi.builder()
                .group("flashcards")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI flashcardsOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Flashcards API")
                        .description("Flashcards backend application")
                        .version("v0.0.1"));
    }
}

package ru.greemlab.managertask.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;

/**
 * Конфигурация для Swagger UI и OpenAPI.
 * Описывает информацию о сервисе и настройки безопасности для API.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Описание OpenAPI для Swagger UI.
     * Указывает информацию о версии, описании и безопасности API.
     *
     * @return OpenAPI конфигурация.
     */
    @Bean
    public OpenAPI customerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Medicines API")
                        .description("API документация для приложения Spring Medicines")
                        .version("1.0.0"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
    }
}

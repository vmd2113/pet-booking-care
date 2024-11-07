package com.duongw.universalpetcare.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {


    @Bean
    public OpenAPI openAPI(
            @Value("${open.api.title}") String title,
            @Value("${open.api.server}") String url_server1,
            @Value("${open.api.description}") String description_server1
           ){

        return new OpenAPI().info(new Info().title(title)
                        .version("1.0.0")
                        .description("")
                        .license(new License().name("API license").url("http://domain.vn/license")))
                .servers(List.of(new Server().url(url_server1).description(description_server1)));

    }

    @Bean
    public GroupedOpenApi publicApi(@Value("${open.api.service.api-docs}") String apiDocs) {
        return GroupedOpenApi.builder()
                .group(apiDocs) // /v3/api-docs/api-service
                .packagesToScan("com.duongw.universalpetcare.controller")
                .build();
    }

}

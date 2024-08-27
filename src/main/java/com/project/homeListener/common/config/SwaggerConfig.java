package com.project.homeListener.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    private Info apiInfo() {
        return new Info()
                .title("home listener api docs") // API의 제목
                .description("API docs") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }


    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl("https://homerecorder.kro.kr");

        String jwt = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
        );

        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(Arrays.asList(server))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}

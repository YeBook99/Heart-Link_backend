package com.ss.heartlinkapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    private final String TITLE = "title";
    private final String DESCRIPTION = "description";
    private final String VERSION = "V1.0.0";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ss.heartlinkapi")) // 실제 패키지로 변경
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/"); // 기본 경로 설정
    }
}
package com.cuv.admin.web.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 설정
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "카통령 관리자 API",
                description = "CARTONGRYUNG-ADMIN API Documentation",
                version ="v1",
                contact = @Contact(
                        name = "Park Sung Ha",
                        email = "sh924@naver.com"
                ),
                license = @License(
                        name = "MySQL 8.0.38 , Java 17 , SpringBoot 3.2.5 , Thymeleaf ,  JPA , QueryDSL"
                )
        )
)
public class SwaggerConfig {
}

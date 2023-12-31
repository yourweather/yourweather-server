package com.umc.yourweather.config;

import com.umc.yourweather.jwt.filter.CustomLoginFilter;
import com.umc.yourweather.jwt.filter.CustomOAuthLoginFilter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SpringDocsConfig {
    @Bean
    public OpenAPI openAPI(@Value("${springdoc.version}") String version) {

        Info info = new Info()
                .title("yourweather API 문서") // 타이틀
                .version(version) // 문서 버전
                .description("잘못된 부분이나 오류 발생 시 바로 말씀해주세요."); // 문서 설명


        // Security 스키마 설정
        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);

        // Security 요청 설정
        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("JWT");

        return new OpenAPI()
                // Security 인증 컴포넌트 설정
                .components(new Components().addSecuritySchemes("JWT", bearerAuth))
                // API 마다 Security 인증 컴포넌트 설정
                .addSecurityItem(addSecurityItem)
                .info(info);
    }

    @Bean
    OpenApiCustomizer customLoginEndpointCustomizer(
            CustomLoginFilter loginFilter) {
        return new LoginEndpointCustomizer<>()
                .loginEndpointCustomizer(loginFilter, "login");
    }

    @Bean
    OpenApiCustomizer customOAuthLoginEndpointCustomizer(
            CustomOAuthLoginFilter loginFilter) {
        return new LoginEndpointCustomizer<>()
                .oauthLoginEndpointCustomizer(loginFilter, "login");
    }
}

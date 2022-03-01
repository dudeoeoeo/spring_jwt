package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 서버가 응답을 할 때 json 을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        // ex) axios or fetch 로 서버에 요청을 할 때 데이터를 받을 수 있게 할지 결정
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // 모든 ip에 응답을 허용
        config.addAllowedHeader("*"); // 모든 header 에 응답을 허용
        config.addAllowedMethod("*"); // 모든 method 방식에 응답을 허용 ex) GET, POST, PUT, DELETE, PATCH

        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}

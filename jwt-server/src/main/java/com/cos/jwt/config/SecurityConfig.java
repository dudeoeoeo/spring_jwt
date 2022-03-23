package com.cos.jwt.config;

import com.cos.jwt.config.jwt.JWTLoginFilter;
import com.cos.jwt.config.jwt.JWTUtil;
import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.config.jwt.JwtAuthorizationFilter;
import com.cos.jwt.filter.MyFilter1;
import com.cos.jwt.filter.MyFilter3;
import com.cos.jwt.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    private JWTUtil jwtUtil = new JWTUtil();

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 사용하지 않는 STATELESS 상태로 만들겠다
        .and()
        .addFilter(corsFilter) // 모든 요청이 corsFilter 를 통과하도록 설정
        .formLogin().disable()
        // Authorization: ID, PW 를 붙여서 요청 => 보안이 좋지 않음 => 때문에 https 를 사용해야 함
        // Authorization: Token 사용
        // Basic 은 ID, PW 사용 / Bearer Token 사용
        .httpBasic().disable()
//        .addFilter(new JwtAuthenticationFilter(authenticationManager())) // AuthenticationManager
        .addFilter(new JWTLoginFilter(jwtUtil, objectMapper, authenticationManager()))
        .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
        .authorizeRequests()
        .antMatchers("/api/v1/user/**")
        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/manager/**")
        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
        .antMatchers("/api/v1/admin/**")
        .access("hasRole('ROLE_ADMIN')")
        .anyRequest()
        .permitAll();
//        .and();
//        .exceptionHandling().authenticationEntryPoint();
    }
}

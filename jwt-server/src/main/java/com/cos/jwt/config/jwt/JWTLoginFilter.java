package com.cos.jwt.config.jwt;

import com.cos.jwt.dto.UserLogin;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public JWTLoginFilter(JWTUtil jwtUtil, ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/login");
    }

    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UserLogin userLogin = objectMapper.readValue(request.getInputStream(), UserLogin.class);
        System.out.println("JWTLoginFilter " + userLogin);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userLogin.getUsername(), userLogin.getPassword(), null
        );

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        response.addHeader("Authentication", "Bearer " + jwtUtil.generate(String.valueOf(user.getId())));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        System.out.println("unsuccessfulAuthentication" + failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }
}

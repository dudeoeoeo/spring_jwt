package com.cos.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cos.jwt.config.jwt.JwtProperties;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//public class MyFilter1 implements Filter {
public class MyFilter1 extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("OncePerRequestFilter MyFilter1 called");

        String jwtToken = request.getHeader(JwtProperties.HEADER_STRING);
        System.out.println("header: " + jwtToken);

//        if (jwtToken != null || !jwtToken.startsWith(JwtProperties.HEADER)) {
//            throw new IllegalArgumentException("invalid token, attack the request");
//        }

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build();
        System.out.println("jwtVerifier: " + jwtVerifier);

        try {
            DecodedJWT jwt = jwtVerifier.verify(jwtToken);
        } catch (JWTVerificationException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        System.out.println("필터1 called");
//        chain.doFilter(request, response); // 여기를 거쳐서 계속 진행할 수 있도록 chain 에 request, response를 넘겨준다
//    }

}

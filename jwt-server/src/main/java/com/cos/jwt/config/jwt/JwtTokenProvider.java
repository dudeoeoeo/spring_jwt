package com.cos.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

public class JwtTokenProvider {

    public Token generateToken(Authentication authentication, Long userId, String username) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String accessToken = JWT.create()
                .withSubject("kay_token")
                .withExpiresAt(new Date(System.currentTimeMillis() + (JwtProperties.EXPIRATION_TIME)))
                .withClaim("id", userId) // 비공개 claim 으로 넣고 싶은 값을 넣을 수 있다
                .withClaim("username", username)
                .withClaim("auth", authorities)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        String refreshToken = JWT.create()
                .withSubject("kay_token")
                .withExpiresAt(new Date(System.currentTimeMillis() + (JwtProperties.EXPIRATION_TIME * 1440)))
                .withClaim("id", userId) // 비공개 claim 으로 넣고 싶은 값을 넣을 수 있다
                .withClaim("username", username)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return Token.builder()
                .accessToken(JwtProperties.HEADER + accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String renewAccessToken(User user) {
        String accessToken = JWT.create()
                .withSubject("kay_token")
                .withExpiresAt(new Date(System.currentTimeMillis() + (JwtProperties.EXPIRATION_TIME)))
                .withClaim("id", user.getId()) // 비공개 claim 으로 넣고 싶은 값을 넣을 수 있다
                .withClaim("username", user.getUsername())
                .withClaim("auth", user.getRoleList().stream().map(s -> s).collect(Collectors.joining(",")))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return accessToken;
    }

    public Long getUserId (String token) {
        if(token.startsWith(JwtProperties.HEADER))
            token = token.replace(JwtProperties.HEADER, "");
        return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim("id").asLong();
    }
}

package com.cos.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cos.jwt.config.jwt.JwtProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;

@SpringBootTest
public class JWTControllerTest {

    @DisplayName("Jwt 토큰 발급")
    @Test
    void JWT_토큰을_발급한다() {
        String accessToken = JWT.create()
                .withSubject("kay_token")
                .withClaim("exp", Instant.now().getEpochSecond() + 3)
                .withClaim("username", "user1")
                .sign(Algorithm.HMAC512("test"));

        System.out.println("토큰: " + accessToken);

        DecodedJWT decoded = JWT.decode(accessToken);

        System.out.println("decode: " + decoded);
    }
}

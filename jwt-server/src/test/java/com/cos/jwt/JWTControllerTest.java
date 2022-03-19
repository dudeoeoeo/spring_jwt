package com.cos.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cos.jwt.config.jwt.JwtProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JWTControllerTest {

    static void printClaim(String key, Claim value) {
        if(value.isNull()) {
            System.out.printf("%s: {str}%s\n", key, value.asString());
            return;
        }
        if(value.asString() != null) {
            System.out.printf("%s:{str}%s\n", key, value.asString());
            return;
        }
        if(value.asLong() != null) {
            System.out.printf("%s:{lng}%d\n", key, value.asLong());
            return;
        }
        if(value.asInt() != null) {
            System.out.printf("%s:{int}%d\n", key, value.asInt());
            return;
        }
        if(value.asBoolean() != null) {
            System.out.printf("%s:{bol}%b\n", key, value.asBoolean());
            return;
        }
        if(value.asDate() != null) {
            System.out.printf("%s:{dte}%d\n", key, value.asDate().toString());
            return;
        }
        if(value.asDouble() != null) {
            System.out.printf("%s:{dbl}%f\n", key, value.asDouble());
            return;
        }
        String [] values = value.asArray(String.class);
        if (values != null) {
            System.out.printf("%s:{arr}%s\n", key, Stream.of(values).collect(Collectors.toList()));
            return;
        }
        Map valueMap = value.asMap();
        if (valueMap != null) {
            System.out.printf("%s:{map}%s\n", key, valueMap);
            return;
        }

        System.out.println("======>> unknown type for : " + key);
    }


    @DisplayName("Jwt 토큰 발급")
    @Test
    void JWT_토큰을_발급한다() throws InterruptedException {

        Algorithm algorithm = Algorithm.HMAC512("test");

        String accessToken = JWT.create()
                .withSubject("kay_token")
                .withClaim("exp", Instant.now().getEpochSecond() + 3)
                .withClaim("username", "user1")
                .withArrayClaim("role", new String[]{"ROLE_ADMIN", "ROLE_USER"})
                .sign(algorithm);

        System.out.println("토큰: " + accessToken);

        DecodedJWT decoded = JWT.require(algorithm).build().verify(accessToken);

        printClaim("typ", decoded.getHeaderClaim("typ"));
        printClaim("alg", decoded.getHeaderClaim("alg"));
        decoded.getClaims().forEach(JWTControllerTest::printClaim);

        Thread.sleep(4000);

        assertThrows(TokenExpiredException.class, () -> {
            JWT.require(algorithm).build().verify(accessToken);
        });
    }
}

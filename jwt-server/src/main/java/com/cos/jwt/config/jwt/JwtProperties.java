package com.cos.jwt.config.jwt;

public interface JwtProperties {

    String HEADER = "Bearer ";
    String SECRET = "kay";
    String HEADER_STRING = "Authorization";
    int EXPIRATION_TIME = 60000 * 10;
}
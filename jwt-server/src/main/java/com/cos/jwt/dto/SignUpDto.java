package com.cos.jwt.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SignUpDto {

    @NotNull
    private String username;
    @NotNull
    private String password;

}

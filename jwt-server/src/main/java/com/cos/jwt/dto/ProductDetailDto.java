package com.cos.jwt.dto;

import com.cos.jwt.model.Option;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductDetailDto {

    private Long Id;
    private String name;
    private String description;
    private int price;
    private Option option;
}

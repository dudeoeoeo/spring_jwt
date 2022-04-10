package com.cos.jwt.dto;

import com.cos.jwt.model.Cart;
import com.cos.jwt.model.Option;
import com.cos.jwt.model.Product;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CartInfoDto {

    private Cart cart;
    private Product product;
    private Option option;
}

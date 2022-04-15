package com.cos.jwt.dao;

import com.cos.jwt.dto.CartInfoDto;
import com.cos.jwt.model.Cart;

import java.util.List;

public interface CartDAO {

    Cart getCartItem(Long cartId);
//    List<CartInfoDto> getCartItem(Long cartId);
}

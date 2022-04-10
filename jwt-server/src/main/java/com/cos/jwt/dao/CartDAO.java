package com.cos.jwt.dao;

import com.cos.jwt.dto.CartInfoDto;

import java.util.List;

public interface CartDAO {

    List<CartInfoDto> getCartItem(Long cartId);
}

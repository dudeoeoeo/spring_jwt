package com.cos.jwt.dao;

import com.cos.jwt.dto.CartInfoDto;
import com.cos.jwt.model.QCart;
import com.cos.jwt.model.QOption;
import com.cos.jwt.model.QProduct;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.cos.jwt.model.QCart.cart;
import static com.cos.jwt.model.QOption.option;
import static com.cos.jwt.model.QProduct.product;

@RequiredArgsConstructor
public class CartDAOImpl implements CartDAO{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CartInfoDto> getCartItem(Long cartId) {
        List<CartInfoDto> contents = queryFactory
                .select(Projections.bean(CartInfoDto.class,
                        cart,
                        product))
                .from(cart, product)
//                .join(product.cart, cart)
//                .join(option.product, product)
                .where(cart.id.eq(cartId))
                .fetch();

        return contents;
    }
}

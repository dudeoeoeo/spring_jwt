package com.cos.jwt.dao;

import com.cos.jwt.dto.CartInfoDto;
import com.cos.jwt.model.*;
import com.google.gson.Gson;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.cos.jwt.model.QCart.cart;
import static com.cos.jwt.model.QOption.option;
import static com.cos.jwt.model.QProduct.product;

@RequiredArgsConstructor
public class CartDAOImpl implements CartDAO {

    private final JPAQueryFactory queryFactory;

    /**
     * Cart 테이블에 상품이 여러개가 담기고
     * Product 테이블에는 카트가 한 개만 있는 것이 아니므로
     * Product 테이블에는 Cart 가 불필요
     * 마찬가지로 Option 테이블에는 상품이 여러 개가
     * 있을 수 있으므로 Product 불필요
     * @param cartId
     * @return
     */
    @Override
    public Cart getCartItem(Long cartId) {
        List<Tuple> tupleList = queryFactory
                .select(cart, product)
                .from(cart, product)
                .join(cart.productList, product)
                .fetchJoin()
                .distinct()
                .where(cart.id.eq(cartId))
                .fetch();
        System.out.println("=============================== CartInfoDto ================================");
        Cart cart = tupleList.get(0).get(QCart.cart);
        Gson gson = new Gson();
        String json = gson.toJson(cart);
        System.out.println(json);
        System.out.println("=============================== CartInfoDto End ================================");
        return cart;
    }

//    @Override
//    public List<CartInfoDto> getCartItem(Long cartId) {
//        List<CartInfoDto> contents = queryFactory
//                .select(Projections.bean(CartInfoDto.class,
//                        cart,
//                        product,
//                        option))
//                .from(cart, product, option)
//                .join(option.product, product)
//                .distinct()
//                .where(cart.id.eq(cartId))
//                .fetch();
//
//        return contents;
//    }
}

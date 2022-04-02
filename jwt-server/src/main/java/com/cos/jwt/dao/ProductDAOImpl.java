package com.cos.jwt.dao;

import com.cos.jwt.dto.ProductInfoDto;
import com.cos.jwt.model.QOption;
import com.cos.jwt.model.QProduct;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.cos.jwt.model.QOption.option;
import static com.cos.jwt.model.QProduct.product;

@RequiredArgsConstructor
public class ProductDAOImpl implements ProductDAO {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductInfoDto> productInfo(Pageable pageable) {
        List<ProductInfoDto> contents = queryFactory
                .select(Projections.bean(ProductInfoDto.class,
                        product.name,
                        product.description,
                        product.price,
                        option))
                .from(product, option)
                .join(option.product, product)
                .where(product.id.gt(1))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<ProductInfoDto> countQuery = queryFactory
                .select(Projections.bean(ProductInfoDto.class,
                        product.name,
                        product.description,
                        product.price,
                        option))
                .from(product, option)
                .join(option.product, product)
                .where(product.id.gt(1));

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchCount);
    }
}

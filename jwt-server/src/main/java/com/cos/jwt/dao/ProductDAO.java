package com.cos.jwt.dao;

import com.cos.jwt.dto.ProductDetailDto;
import com.cos.jwt.dto.ProductInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductDAO {

    Page<ProductInfoDto> productInfo(Pageable pageable);
    List<ProductDetailDto> productDetail(Long productId);
}

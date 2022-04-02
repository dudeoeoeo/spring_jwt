package com.cos.jwt.dao;

import com.cos.jwt.dto.ProductInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductDAO {

    Page<ProductInfoDto> productInfo(Pageable pageable);
}

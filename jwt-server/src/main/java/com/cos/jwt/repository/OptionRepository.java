package com.cos.jwt.repository;

import com.cos.jwt.model.Option;
import com.cos.jwt.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {

//    List<Option> findAllByProduct(Product product);
}

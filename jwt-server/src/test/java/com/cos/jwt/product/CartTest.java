package com.cos.jwt.product;

import com.cos.jwt.model.Cart;
import com.cos.jwt.model.Option;
import com.cos.jwt.model.Product;
import com.cos.jwt.repository.CartRepository;
import com.cos.jwt.repository.OptionRepository;
import com.cos.jwt.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private JPAQueryFactory query;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    private ProductHelper productHelper = new ProductHelper();

    @BeforeEach
    void 기본_상품_옵션_생성() {
        List<Product> productList = productHelper.상품생성();
        productRepository.saveAll(productList);

        List<Option> optionList = productHelper.상품옵션생성();
        optionRepository.saveAll(optionList);
    }
    @Test
    void 상품헬퍼테스트() {
        List<Product> productList = productHelper.상품생성();
        productRepository.saveAll(productList);

        List<Product> products = productRepository.findAll();

        products.forEach(System.out::println);

        List<Option> optionList = productHelper.상품옵션생성();
        optionRepository.saveAll(optionList);

        List<Option> options = optionRepository.findAll();

        options.forEach(System.out::println);
    }

    @Test
    void 상품_장바구니_추가_테스트() {
        Cart cart = new Cart();

        List<Product> products = productRepository.findAll();
        cart.setProductList(new ArrayList<>(Arrays.asList(products.get(0), products.get(1), products.get(2))));

        Cart savedCart = cartRepository.save(cart);

        System.out.println(savedCart.toString());
        assertEquals(3, savedCart.getProductList().size());
    }
}

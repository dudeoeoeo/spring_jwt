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

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private EntityManager entityManager;

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

    /**
     * 영속 상태를 유지하면서 테스트 진행을 위해 @Transactional 을 붙여준다
     * 현재 Cart entity 에 Product 가 존재하고 Product entity 에도 Cart 가 존재한다.
     * 하지만 Product entity 에 굳이 Cart 가 존재할 필요가 있을까
     * TODO: 장바구니는 유저와 1:1 매칭을 하고 장바구니에는 여러가지 상품을 담을 수 있고
     *       상품은 장바구니의 정보를 가지고 있을 이유가 없다
     */
    @Test
    @Transactional
    void 상품_추가_삭제_테스트() {
        Cart cart = new Cart();

        List<Product> products = productRepository.findAll();
        cart.setProductList(new ArrayList<>(Arrays.asList(products.get(0), products.get(1), products.get(2))));

        Cart savedCart = cartRepository.save(cart);
        Long cartId = savedCart.getId();
        Product newProduct = products.get(4);

        savedCart.getProductList().add(newProduct);

        Cart newSavedCart = cartRepository.save(savedCart);
//        Optional<Cart> newSavedCart = cartRepository.findById(cartId);

        System.out.println(newSavedCart.toString());
        assertEquals(4, newSavedCart.getProductList().size());

        newSavedCart.getProductList().remove(newSavedCart.getProductList().size() - 1);

        newSavedCart = cartRepository.save(newSavedCart);

        assertEquals(3, newSavedCart.getProductList().size());
    }

}

package com.cos.jwt.product;

import com.cos.jwt.model.Option;
import com.cos.jwt.model.Product;
import com.cos.jwt.repository.OptionRepository;
import com.cos.jwt.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private JPAQueryFactory query;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    @BeforeEach
    void 기본상품등록() {
        String [] names = new String[]{"맨투맨", "후드티", "셔츠", "데님바지", "수면양말", "흰티", "검은티", "멜빵", "조끼"};
        int [] prices = new int[]{10000, 15000, 20000, 22000, 3000, 5000, 8000, 12000, 9500};
        for(int i = 1; i < 10; i++) {
            Product product = new Product();
                    product.setName(names[i-1]);
                    product.setPrice(prices[i-1]);
                    product.setDescription("상품설명"+i);
            productRepository.save(product);
        }

        String [] sizes = new String[]{"S", "M", "L", "XL", "XXL"};
        String [] colors = new String[]{"white", "black", "yellow", "purple", "blue"};
        int [] extraPrices = new int[]{3000, 2000, 5000, 8000, 6000};
        int [] stocks = new int[]{90, 80, 42, 66, 81};
        for (int i = 0; i < 5; i++) {
            Option option = Option.builder()
                    .size(sizes[i])
                    .color(colors[i])
                    .extraPrice(extraPrices[i])
                    .stock(stocks[i])
                    .build();
            optionRepository.save(option);
        }
    }
    @AfterEach
    void 테스트가_끝난_후() {
        productRepository.deleteAll();
        optionRepository.deleteAll();
    }

    @Test
    void 상품이_테이블에_등록되었는지_확인() {
        List<Product> products = productRepository.findAll();
        products.forEach(System.out::println);
    }
}

package com.cos.jwt.product;

import com.cos.jwt.dto.ProductDetailDto;
import com.cos.jwt.dto.ProductInfoDto;
import com.cos.jwt.model.Option;
import com.cos.jwt.model.Product;
import com.cos.jwt.repository.OptionRepository;
import com.cos.jwt.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

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

    @Transactional
    @BeforeEach
    void 기본상품등록() {
        String [] names = new String[]{"맨투맨", "후드티", "셔츠", "데님바지", "수면양말", "흰티", "검은티", "멜빵", "조끼"};
        int [] prices = new int[]{10000, 15000, 20000, 22000, 3000, 5000, 8000, 12000, 9500};
        List<Product> productList = new ArrayList<>();
        for(int i = 1; i < 10; i++) {
            Product product = new Product();
                    product.setName(names[i-1]);
                    product.setPrice(prices[i-1]);
                    product.setDescription("상품설명"+i);
            productRepository.save(product);
            productList.add(product);
        }

        String [] sizes = new String[]{"S", "M", "L", "XL", "XXL"};
        String [] colors = new String[]{"white", "black", "yellow", "purple", "blue"};
        int [] extraPrices = new int[]{3000, 2000, 5000, 8000, 6000};
        int [] stocks = new int[]{90, 80, 42, 66, 81};
        for (int i = 0; i < 5; i++) {
            Option option = new Option();
            option.setSize(sizes[i]);
            option.setColor(colors[i]);
            option.setExtraPrice(extraPrices[i]);
            option.setStock(stocks[i]);
            option.setProduct(productList.get(1));
            optionRepository.save(option);
//            productList.get(i).setOptions(Collections.singletonList(option));
        }
//        productRepository.saveAll(productList);
    }
    @AfterEach
    void 테스트가_끝난_후() {
//        productRepository.deleteAll();
//        optionRepository.deleteAll();
    }

    /**
     * Error: failed to lazily initialize a collection of role: com.cos.jwt.model.Product.options, could not initialize proxy - no Session
     *        org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.cos.jwt.model.Product.options,
     *        could not initialize proxy - no Session
     * TODO: JPA 의 영속성 컨텍스트는 보통 트랜잭션과 생명주기가 같기 때문에
     *       영속성 상태가 끝났을 때 Entity 의 필요한 값이 있을 때 쿼리를 날려
     *       Proxy 객체를 채우지 않는다.
     *
     * CLEAR: 현재 Product 는 Option 을 필수 값이 아닌 선택 값으로
     *        가지고 있으므로 Product Entity 에 Option 을 반드시
     *        가지고 있을 필요가 없다.
     */
    @Transactional
    @Test
    void 상품이_테이블에_등록되었는지_확인() {
        System.out.println("상품 테스트");
        List<Option> options = optionRepository.findAll();
        List<Product> products = productRepository.findAll();
        products.forEach(System.out::println);
        options.forEach(System.out::println);
    }

    @Transactional
    @Test
    void 상품_등록_테스트() {
        Product product = new Product();
        product.setName("안마의자");
        product.setDescription("앉으면 잠이 오는 안마의자");
        product.setPrice(2380000);

        Product savedProduct = productRepository.save(product);

        Option option = new Option();
        option.setId(Long.valueOf(16));
        option.setProduct(savedProduct);
        option.setColor("white");
        option.setStock(1);
        option.setExtraPrice(70000);

        option = optionRepository.save(option);
//        option = optionRepository.findById(Long.valueOf(16));
        System.out.println(option.toString());

//        assertEquals(Long.valueOf(15), option.getProduct().getId());
        assertEquals("안마의자", option.getProduct().getName());
        assertEquals("앉으면 잠이 오는 안마의자", option.getProduct().getDescription());
        assertEquals(2380000, option.getProduct().getPrice());
    }

    @Transactional
    @Test
    void 상품_수정_테스트() {
        Product product = new Product();
        product.setName("안마의자");
        product.setDescription("앉으면 잠이 오는 안마의자");
        product.setPrice(2380000);

        Product savedProduct = productRepository.save(product);

        String newDescription = "새로운 아이템";
        int newPrice = 1900000;

        savedProduct.setDescription(newDescription);
        savedProduct.setPrice(newPrice);

        Product updatedProduct = productRepository.save(savedProduct);

        assertEquals(newDescription, updatedProduct.getDescription());
        assertEquals(newPrice, updatedProduct.getPrice());
    }

    @Transactional
    @Test
    void 상품_등록후_옵션추가() {
        Product product = new Product();
        product.setName("안마의자");
        product.setDescription("앉으면 잠이 오는 안마의자");
        product.setPrice(2380000);

        Product savedProduct = productRepository.save(product);

        Option option1 = new Option();
        option1.setId(Long.valueOf(16));
        option1.setProduct(savedProduct);
        option1.setColor("black");
        option1.setStock(100);
        option1.setExtraPrice(150000);

        Option option2 = new Option();
        option2.setId(Long.valueOf(17));
        option2.setProduct(savedProduct);
        option2.setColor("pink");
        option2.setStock(30);
        option2.setExtraPrice(2000000);

        optionRepository.saveAll(Arrays.asList(option1, option2));

        List<Option> optionList = optionRepository.findAllByProduct(savedProduct);

        assertEquals(2380000, optionList.get(0).getProduct().getPrice());
        assertEquals(2, optionList.size());
        assertEquals(4380000, optionList.get(1).getProduct().getPrice() + optionList.get(1).getExtraPrice());
        assertEquals(2530000, optionList.get(0).getProduct().getPrice() + optionList.get(0).getExtraPrice());
    }

    @Transactional
    @Test
    void 상품_정보_가져오기() {
        PageRequest request = PageRequest.of(1, 5);
        Page<ProductInfoDto> productInfoDtos = productRepository.productInfo(request);
        productInfoDtos.get().forEach(System.out::println);
    }

    @Transactional
    @Test
    void 상품디테일정보() {
        List<ProductDetailDto> productDetailList = productRepository.productDetail(Long.valueOf(2));

        productDetailList.forEach(System.out::println);

        assertEquals(5, productDetailList.size());
    }
}

package com.cos.jwt.product;

import com.cos.jwt.dto.CartInfoDto;
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

//    @BeforeEach
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
     *       상품은 장바구니의 정보를 가지고 있을 이유가 없다 ?
     *       단방향 @OneToMany 를 사용하면 나타날 수 있는 문제점
     *       1. 단방향 Entity 에 @JoinColumn 을 명시하지 않을 경우 JPA가 자동으로 JoinTable을 생성하려 한다.
     *       2. Insert Null
     *       3. 불필요한 Update 쿼리
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

    @Test
    @Transactional
    void 카트에_담은_상품들_가져오기() {

        Cart cart = new Cart();
        List<Product> products = productRepository.findAll();

        cart.setProductList(new ArrayList<>(Arrays.asList(products.get(0), products.get(1), products.get(2), products.get(4))));

        Cart savedCart = cartRepository.save(cart);
        Optional<Cart> sCart = cartRepository.findById(savedCart.getId());

        List<CartInfoDto> cartItem = cartRepository.getCartItem(sCart.get().getId());
        System.out.println("\n\n\n cartItem \n\n\n" + cartItem.toString());

        Optional<Cart> c1 = cartRepository.findById(sCart.get().getId());
        System.out.println("\n\n\n\n c1 \n\n\n" + c1.get().toString());
    }

    /**
     * TODO: product 필드에서 Cart 를 제거..
     */
    @Test
    @Transactional
    void 양방향테스트() {
        String [] names = new String[]{"맨투맨", "후드티", "셔츠", "데님바지", "수면양말", "흰티", "검은티", "멜빵", "조끼"};
        int [] prices = new int[]{10000, 15000, 20000, 22000, 3000, 5000, 8000, 12000, 9500};
        List<Product> productList = new ArrayList<>();
        for(int i = 1; i < 10; i++) {
            Product product = new Product();
            product.setName(names[i-1]);
            product.setPrice(prices[i-1]);
            product.setDescription("상품설명"+i);
            productList.add(product);
        }
        String [] sizes = new String[]{"S", "M", "L", "XL", "XXL"};
        String [] colors = new String[]{"white", "black", "yellow", "purple", "blue"};
        int [] extraPrices = new int[]{3000, 2000, 5000, 8000, 6000};
        int [] stocks = new int[]{90, 80, 42, 66, 81};
        List<Option> optionList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Option option = new Option();
            option.setSize(sizes[i]);
            option.setColor(colors[i]);
            option.setExtraPrice(extraPrices[i]);
            option.setStock(stocks[i]);
            option.setProduct(productList.get(i));
            optionList.add(option);
            productList.get(i).getOptions().add(option);
        }
        productRepository.saveAll(productList);
        Cart cart = new Cart();

        cart.setProductList(new ArrayList<>(Arrays.asList(productList.get(0), productList.get(1), productList.get(2), productList.get(4))));
        Cart savedCart = cartRepository.save(cart);

//        System.out.println("\n\n\n savedCart \n\n\n ===========>    " + savedCart.getProductList().get(0).getOptions().toString() );
        List<CartInfoDto> cartItem = cartRepository.getCartItem(savedCart.getId());
//        System.out.println("\n\n\n cartItem \n\n\n" + cartItem.toString());
        System.out.println("\n\n=======================================================");
        cartItem.forEach(System.out::println);
        System.out.println("\n\n=======================================================");
    }
}

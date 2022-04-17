package com.cos.jwt.product;

import com.cos.jwt.constant.OrderStatus;
import com.cos.jwt.model.Option;
import com.cos.jwt.model.Order;
import com.cos.jwt.model.Product;
import com.cos.jwt.repository.CartRepository;
import com.cos.jwt.repository.OptionRepository;
import com.cos.jwt.repository.OrderRepository;
import com.cos.jwt.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void 상품_옵션_생성() {
        List<Product> productList = new ArrayList<>();
        List<Option> optionList = new ArrayList<>();
        String [] names = new String[]{"맨투맨", "후드티", "셔츠", "데님바지", "수면양말", "흰티", "검은티", "멜빵", "조끼"};
        int [] prices = new int[]{10000, 15000, 20000, 22000, 3000, 5000, 8000, 12000, 9500};

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
        for (int i = 0; i < 5; i++) {
            Option option = new Option();
            option.setSize(sizes[i]);
            option.setColor(colors[i]);
            option.setExtraPrice(extraPrices[i]);
            option.setStock(stocks[i]);
            optionList.add(option);
            productList.get(i).getOptions().add(option);
        }
        optionRepository.saveAll(optionList);
        productRepository.saveAll(productList);
    }

    @Test
    @Transactional
    void 주문_테스트() {
        final List<Product> products = productRepository.findAll();

        Order order = new Order();

        order.setProducts(Collections.singletonList(products.get(0)));
        order.setStatus(OrderStatus.ORDERED);

        final Order saveOrder = orderRepository.save(order);

        assertEquals(products.get(0).getId(), saveOrder.getProducts().get(0).getId());
        assertEquals(products.get(0).getPrice(), saveOrder.getProducts().get(0).getPrice());
    }
}

package com.cos.jwt.product;

import com.cos.jwt.model.Option;
import com.cos.jwt.model.Product;
import com.cos.jwt.repository.OptionRepository;
import com.cos.jwt.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ProductHelper {

    List<Product> 상품생성() {
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
        return productList;
    }

    List<Option> 상품옵션생성() {
        List<Option> optionList = new ArrayList<>();

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
        }
        return optionList;
    }
}

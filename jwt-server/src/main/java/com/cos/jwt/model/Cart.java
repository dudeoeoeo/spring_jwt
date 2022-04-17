package com.cos.jwt.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    private Long id;

    /**
     * MappedBy: 관계의 주인 = Foreign Key
     * JPA 에서 관계를 표현함에 있어 중요한 것은 Foreign Key 가 바라보는 대상이다.
     * 현재 Cart 와 Product 의 관계를 보면 Product 테이블의 cart_id 는 FK 이다.
     * 따라서 Product 가 현재 이 '관계의 주인' 이다.
     *  Product 가 관계의 주인인 이유는 Cart 테이블의 Product_id 는 여러개일 수 있기 때문에
     *  특정 관계를 찾기 어렵다.
     */
//    @OneToMany(mappedBy = "cart", orphanRemoval = true, cascade = CascadeType.ALL)
//    @Column(name = "product_id")
    @OneToMany
    private List<Product> productList = new ArrayList<>();
}

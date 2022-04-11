package com.cos.jwt.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@ToString
@Table(name = "product")
public class Product {

    @Id @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private int price;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToMany(mappedBy = "product")
    private List<Option> options = new ArrayList<>();

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("id: " + this.id);
//        sb.append("name: " + this.name);
//        sb.append("description: " + this.description);
//        sb.append("price: " + this.price);
//        if (this.options.size() > 0)
//            sb.append("options: " + this.options.toString());
//        return String.valueOf(sb);
//    }
}

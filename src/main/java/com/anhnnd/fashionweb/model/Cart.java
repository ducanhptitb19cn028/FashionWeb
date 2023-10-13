package com.anhnnd.fashionweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="tbl_cart")
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    private Long id;
    private Integer product_quantity;
    private float total_price;
    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}
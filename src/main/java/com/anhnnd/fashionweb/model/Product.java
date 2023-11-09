package com.anhnnd.fashionweb.model;


import lombok.*;

import jakarta.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_product")

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private float price;
    private String description;
    private String image;
    private String size;
    private String gender;
    private int quantity;
    private int quantitysold;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private List<ProductHistory> productHistory;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private List<Review> listReview;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "admin_id")
    private Admin admin;
}
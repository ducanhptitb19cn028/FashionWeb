package com.anhnnd.fashionweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "tbl_product_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "new_quantity")
    private int newQuantity;

    @Column(name = "change_time")
    private LocalDateTime changeTime;

    public ProductHistory(Product product, int newQuantity) {
        this.product = product;
        this.newQuantity = newQuantity;
        this.changeTime = LocalDateTime.now();
    }
}
package com.anhnnd.fashionweb.model;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "tbl_order")
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private String orderStatus;
    private String location;
    private float total_price;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime deliveryDate;
    private LocalDateTime cancelDate;
    private LocalDateTime receiveDate;
    private LocalDateTime paymentDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems = new ArrayList<>();

}
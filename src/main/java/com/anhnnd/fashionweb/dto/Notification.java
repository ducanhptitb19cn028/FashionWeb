package com.anhnnd.fashionweb.dto;


import com.anhnnd.fashionweb.model.Order;
import com.anhnnd.fashionweb.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="tbl_order_notification")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    private String title;
    private String message;
    @OneToOne(cascade = CascadeType.ALL)
    private Order order;
}

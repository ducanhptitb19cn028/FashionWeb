package com.anhnnd.fashionweb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="tbl_review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", unique= false)
    private Product product;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    private String comment;
    @Column(name = "rating")
    private int rating;
    @Column(name = "review_date")
    private LocalDateTime reviewDate;
    @Transient
    private Boolean isUserComment;

    public Review(Product mockProduct, User mockUser, String testReview, int i, LocalDateTime now) {
        this.product = mockProduct;
        this.user = mockUser;
        this.comment = testReview;
        this.rating = i;
        this.reviewDate = now;
    }
}
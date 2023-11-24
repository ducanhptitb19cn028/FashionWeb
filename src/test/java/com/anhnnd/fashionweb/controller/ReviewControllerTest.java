package com.anhnnd.fashionweb.controller;

import com.anhnnd.fashionweb.model.Product;
import com.anhnnd.fashionweb.model.Review;
import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.service.ProductService;
import com.anhnnd.fashionweb.service.ReviewService;
import com.anhnnd.fashionweb.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewControllerTest {


    @Mock
    private ReviewService reviewService;

    @Mock
    private ProductService productService;
    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private ReviewController reviewController;

    @Test
    @Transactional
    @Rollback
    void testAddReview() {
        User mockUser = userService.getUserById(1L);
        Product mockProduct = productService.getProductById(1L);
        Review review = new Review(mockProduct, mockUser, "Test review", 5, LocalDateTime.now());
        reviewService.addReview(review);

    }

    @Test
    void testAddReviewUserIsNull() {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        Review review = new Review();

        // Act
        String result = reviewController.addReview(review, model, session);

        // Assert
        assertEquals("redirect:/login", result);

        // Verify that the reviewService method was not called
        verify(reviewService, never()).addReview(review);
    }

    @Test
    void testShowAddReviewForm() {
        // Arrange
        Long productId = 1L;
        when(session.getAttribute("user")).thenReturn(new User()); // mock user in session
        when(productService.getProductById(productId)).thenReturn(new Product()); // mock product service

        // Act
        String result = reviewController.showAddReviewForm(productId, model, session);

        // Assert
        assertEquals("reviewForm", result);

        // Verify that the model attributes were set
        verify(model, times(1)).addAttribute("user", any(User.class));
        verify(model, times(1)).addAttribute("product", any(Product.class));
    }

    @Test
    void testShowAddReviewFormUserIsNull() {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        String result = reviewController.showAddReviewForm(1L, model, session);

        // Assert
        assertEquals("redirect:/login", result);

        // Verify that the model attributes were not set
        verify(model, never()).addAttribute("user", any(User.class));
        verify(model, never()).addAttribute("product", any(Product.class));
    }
}
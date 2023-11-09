package com.anhnnd.fashionweb.controller;

import com.anhnnd.fashionweb.model.Product;
import com.anhnnd.fashionweb.model.Review;
import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.service.ProductService;
import com.anhnnd.fashionweb.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductService productService;

    @PostMapping("/addReview")
    public String addReview(@ModelAttribute("review") Review review, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        review.setReviewDate(LocalDateTime.now());
        model.addAttribute("user", user);
        Long productId = review.getProduct().getId();
        reviewService.addReview(review);
        return "redirect:/productDetails?id=" + productId;
    }
    @GetMapping("/showAddReviewForm")
    public String showAddReviewForm(@RequestParam Long productId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "reviewForm";
    }
}
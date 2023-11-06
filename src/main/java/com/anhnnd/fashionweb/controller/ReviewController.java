package com.anhnnd.fashionweb.controller;

import com.anhnnd.fashionweb.model.Product;
import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.service.ProductService;
import com.anhnnd.fashionweb.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductService productService;

    @PostMapping("/addReview")
    public String addReview(@RequestParam Long productId, @RequestParam String comment, @RequestParam int rating, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        Product product = productService.getProductById(productId);
        reviewService.addReview(product, user, comment, rating);
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
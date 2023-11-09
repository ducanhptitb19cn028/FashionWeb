package com.anhnnd.fashionweb.controller;

import com.anhnnd.fashionweb.model.Cart;
import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.repository.CartRepository;
import com.anhnnd.fashionweb.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/view")
    public String getCartByCustomerId(
            HttpSession session,
            Model model
    ){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user",user);
        // Get the user ID from the User object
        Long customerId = user.getId();
        // Use the customerId to fetch the Cart
        Cart cart = cartService.findByUserId(customerId);
        if (cart == null) {
            model.addAttribute("error", "you should add something to cart first");
            return "home";
        }
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/add")
    public String addProductToCart(
            HttpSession session,
            @RequestParam(value = "productId") Long productId,
            @RequestParam("quantity") int quantity,
            Model model
    ){
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/login";
            }
            Long customerId = user.getId();
            model.addAttribute("user", user);

            Cart cart = cartService.addProductToCart(customerId, productId, quantity);
            model.addAttribute("cart", cart);
            return "redirect:/cart/view";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @PostMapping("/remove")
    public String removeProductFromCart(
            @RequestParam(value = "id") Long productId,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Handle the case when the user is not logged in
            return "redirect:/login";
        }
        model.addAttribute("user",user);
        // Get the user ID from the User object
        Long customerId = user.getId();

        try {
            Cart cart = cartService.removeProductFromCart(customerId, productId);
            model.addAttribute("cart", cart);
            return "redirect:/cart/view";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/removeAll")
    public String removeAllProductFromCart(
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Handle the case when the user is not logged in
            return "redirect:/login";
        }
        model.addAttribute("user",user);
        // Get the user ID from the User object
        Long customerId = user.getId();
        try {
            Cart cart = cartService.removeAllProduct(customerId);
            model.addAttribute("cart", cart);
            return "redirect:/cart/view";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/increase")
    public String increaseProductQuantity(
            @RequestParam(value = "id") Long productId,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Handle the case when the user is not logged in
            return "redirect:/login";
        }
        model.addAttribute("user",user);
        // Get the user ID from the User object
        Long customerId = user.getId();
        try {
            Cart cart = cartService.increaseProductQuantity(customerId, productId);
            model.addAttribute("cart", cart);
            return "redirect:/cart/view";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/decrease")
    public String decreaseProductQuantity(
            @RequestParam(value = "id") Long productId,
            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Handle the case when the user is not logged in
            return "redirect:/login";
        }
        model.addAttribute("user",user);
        // Get the user ID from the User object
        Long customerId = user.getId();
        try {
            Cart cart = cartService.decreaseProductQuantity(customerId, productId);
            model.addAttribute("cart", cart);
            return "redirect:/cart/view";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
    @GetMapping("/orderForm")
    public String showAddOrderForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Long userId = user.getId();
        model.addAttribute("user", user);
        Cart cart = cartService.viewCartByUserId(userId);
        if (cart == null) {
            model.addAttribute("error", "Cart not found");
            return "home";
        }
        String errorMessage = (String) session.getAttribute("errorMessage");
        model.addAttribute("errorMessage", errorMessage);
        session.removeAttribute("errorMessage");
        model.addAttribute("cart", cart);

        return "orderForm";
    }
}
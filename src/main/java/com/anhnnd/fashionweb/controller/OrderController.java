package com.anhnnd.fashionweb.controller;

import com.anhnnd.fashionweb.model.Cart;
import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.repository.CartRepository;
import com.anhnnd.fashionweb.service.CartService;
import com.anhnnd.fashionweb.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/order")
public class OrderController {


    private final OrderService orderService;
    private final CartService cartService;
    private final CartRepository cartRepository;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService, CartRepository cartRepository) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/orderForm")
    public String showAddOrderForm(HttpSession session, Model model) throws Exception {
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

    @PostMapping("/submitOrder")
    public String submitOrder(HttpSession session, Model model, @RequestParam String address, @RequestParam String paymentMethod) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Long userId = user.getId();
        model.addAttribute("user", user);
        try {
            orderService.addOrder(userId, address, paymentMethod);
            return "redirect:/";
        } catch (Exception e) {
            session.setAttribute("errorMessage", e.getMessage());
            return "redirect:/order/orderForm";
        }
    }
    @GetMapping("/orderHistory")
    public String orderHistory(HttpSession session,Model model){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Long userId = user.getId();
        model.addAttribute("user", user);
        try{
            model.addAttribute("orderhistory",orderService.getAllOrdersByUserId(userId));

        }catch (Exception e) {
            e.printStackTrace();
        }

        return "orderHistory";
    }
}

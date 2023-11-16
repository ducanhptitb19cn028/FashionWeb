package com.anhnnd.fashionweb.controller;

import com.anhnnd.fashionweb.model.Cart;
import com.anhnnd.fashionweb.model.User;
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

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;

    @PostMapping("/submitOrder")
    public String submitOrder(HttpSession session, Model model, @RequestParam String address, @RequestParam String paymentMethod) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Long userId = user.getId();
        model.addAttribute("user", user);
        Cart cart = cartService.findByUserId(userId);
        try {
            if (cart.getCartItems().isEmpty()) {
                model.addAttribute("error", "you should add something to cart first");
                return "redirect:/";
            }
            orderService.addOrder(userId, address, paymentMethod);
            return "redirect:/cart/view";
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
    @PostMapping("/cancelOrder")
    public String cancelOrder(HttpSession session, Model model, @RequestParam("orderId") Long orderId) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        try {
            orderService.cancelOrder(orderId);
            return "redirect:/order/orderHistory";
        } catch (Exception e) {
            session.setAttribute("errorMessage", e.getMessage());
            return "redirect:/order/orderHistory";
        }
    }
    @PostMapping("/confirmreceiveOrder")
    public String confirmOrder(HttpSession session, Model model, @RequestParam("orderId") Long orderId) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        try {
            orderService.customerReceivedOrder(orderId);
            return "redirect:/order/orderHistory";
        } catch (Exception e) {
            session.setAttribute("errorMessage", e.getMessage());
            return "redirect:/order/orderHistory";
        }
    }
}

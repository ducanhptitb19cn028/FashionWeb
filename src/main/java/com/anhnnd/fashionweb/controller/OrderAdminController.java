package com.anhnnd.fashionweb.controller;


import com.anhnnd.fashionweb.model.Admin;
import com.anhnnd.fashionweb.model.Order;
import com.anhnnd.fashionweb.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class OrderAdminController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/pending-order")
    public String showPendingOrderPage(Model model, HttpSession session){
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<Order> pendingOrders = orderService.getOrdersByStatus("Pending");
        model.addAttribute("pendingOrders",pendingOrders);
        return "pendingorder";
    }
    @PostMapping("/update-to-received")
    public String updateToReceived(Model model, HttpSession session,@RequestParam Long orderId) throws Exception {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        orderService.markOrderAsReceived(orderId);
        model.addAttribute("admin", admin);
        return "redirect:/received-order";
    }
    @GetMapping("/received-order")
    public String showReceivedOrderPage(Model model, HttpSession session){
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<Order> receivedOrders = orderService.getOrdersByStatus("Received");
        model.addAttribute("receivedOrders",receivedOrders);
        return "receivedorder";
    }
    @PostMapping("/update-to-delivered")
    public String updateToDelivered(Model model, HttpSession session,@RequestParam Long orderId) throws Exception {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        orderService.markOrderAsDelivered(orderId);
        model.addAttribute("admin", admin);
        return "redirect:/delivered-order";
    }
    @GetMapping("/delivered-order")
    public String showDeliveredOrderPage(Model model, HttpSession session){
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<Order> deliveredOrders = orderService.getOrdersByOrderStatusAndPaymentStatus("Delivered","Pending");
        model.addAttribute("deliveredOrders",deliveredOrders);
        return "deliveredorder";
    }
    @PostMapping("/update-to-paid")
    public String updateToPaid(Model model, HttpSession session,@RequestParam Long orderId) throws Exception {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        orderService.markOrderAsPaid(orderId);
        model.addAttribute("admin", admin);
        return "redirect:/paid-order";
    }
    @GetMapping("/paid-order")
    public String showPaidOrderPage(Model model, HttpSession session){
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<Order> paidOrders = orderService.getOrdersByOrderStatusAndPaymentStatus("Delivered","Paid");
        model.addAttribute("paidOrders",paidOrders);
        return "paidorder";
    }
    @GetMapping("/cancelled-order")
    public String showCancelledOrderPage(Model model, HttpSession session){
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        model.addAttribute("admin", admin);
        List<Order> cancelledOrders = orderService.getOrdersByStatus("Cancelled");
        model.addAttribute("cancelledOrders",cancelledOrders);
        return "cancelledorder";
    }
    @PostMapping("/update-to-cancelled")
    public String updateToCancelled(Model model, HttpSession session,@RequestParam Long orderId) throws Exception {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin-login";
        }
        if (!orderService.getOrderById(orderId).getOrderStatus().equals("Pending")) {
            throw new Exception("Order cannot be cancelled as it's not in pending status.");
        }
        orderService.cancelOrder(orderId);
        model.addAttribute("admin", admin);
        return "redirect:/cancelled-order";
    }
}

package com.anhnnd.fashionweb.service;

import com.anhnnd.fashionweb.model.*;
import com.anhnnd.fashionweb.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.viewAllOrdersByUserId(userId);
    }

    @Transactional
    public void addOrder(Long userId, String address, String paymentMethod) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));

        Cart cart = cartRepository.findByUserId(user.getId());
        Order order = new Order();

        order.setUser(user);
        order.setDate(LocalDateTime.now());
        order.setOrderStatus("Pending");
        order.setPaymentStatus("Pending");
        order.setLocation(address);
        order.setPaymentMethod(paymentMethod);

        List<OrderItem> orderItems = new ArrayList<>();
        List<Product> productsToUpdateStock = new ArrayList<>(); // Collect products to update stock

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();
            int quantityInCart = cartItem.getQuantity();

            if (product.getQuantity() < quantityInCart) {
                throw new Exception("Product " + product.getName() + " is out of stock.");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getQuantity()*cartItem.getProduct().getPrice());
            orderItems.add(orderItem);


            product.setQuantity(product.getQuantity() - quantityInCart);
            product.setQuantitysold(product.getQuantitysold() + quantityInCart);
            productsToUpdateStock.add(product);
        }

        // Save all the OrderItem items
        orderItemRepository.saveAll(orderItems);

        // Update stock for all products
        productRepository.saveAll(productsToUpdateStock);

        order.setOrderItems(orderItems);
        order.setTotal_price(cart.getTotal_price());
        orderRepository.saveAndFlush(order);

        try {
            //delete cart after order
            cartService.removeAllProduct(user.getId());
        } catch (Exception ex) {
            Logger.getLogger(OrderService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Transactional
    public void cancelOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));
        // Update order status to canceled
        order.setCancelDate(LocalDateTime.now());
        order.setOrderStatus("Cancelled");
        orderRepository.save(order);
        // Restore product quantities and quantities sold
        List<OrderItem> orderItems = order.getOrderItems();
        List<Product> productsToUpdate = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            int quantityReturned = orderItem.getQuantity();

            product.setQuantity(product.getQuantity() + quantityReturned);
            product.setQuantitysold(product.getQuantitysold() - quantityReturned);

            productsToUpdate.add(product);
        }
        // Update product quantities
        productRepository.saveAll(productsToUpdate);
    }
    @Transactional
    public void markOrderAsReceived(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));

        if (!order.getOrderStatus().equals("Pending")) {
            throw new Exception("Order cannot be marked as received as it's not in pending status.");
        }
        order.setReceiveDate(LocalDateTime.now());
        order.setOrderStatus("Received");
        orderRepository.save(order);
    }
    @Transactional
    public void markOrderAsDelivered(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));

        if (!order.getOrderStatus().equals("Received")) {
            throw new Exception("Order cannot be marked as delivered as it's not in Received status.");
        }
        order.setDeliveryDate(LocalDateTime.now());
        order.setOrderStatus("Delivered");
        orderRepository.save(order);
    }
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByOrderStatus(status);
    }
    public List<Order> getAll() {
        return orderRepository.findAll();
    }
    public List<Order> getOrdersByOrderStatusAndPaymentStatus(String order_status,String payment_status) {
        return orderRepository.findByOrderStatusAndPaymentStatus(order_status,payment_status);
    }
    public List<Order> getOrdersByMultipleOrderStatusAndPaymentStatus(String order_status,String order_status1,String payment_status) {
        return orderRepository.findByMultipleOrderStatusAndPaymentStatus(order_status,order_status1,payment_status);
    }
    @Transactional
    public void markOrderAsPaid(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));
        if (!order.getOrderStatus().equals("Customer Received")) {
            throw new Exception("Order cannot be marked as paid as it's not received by customer.");
        }
        order.setPaymentDate(LocalDateTime.now());
        order.setPaymentStatus("Paid");
        orderRepository.save(order);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }
    @Transactional
    public void customerReceivedOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found"));
        if (!order.getOrderStatus().equals("Delivered")) {
            throw new Exception("Order cannot be marked as received as it's not in delivered status.");
        }
        order.setReceiveDate(LocalDateTime.now());
        order.setOrderStatus("Customer Received");
        orderRepository.save(order);
    }
}

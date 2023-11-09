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

    public List<Order> getAllOrdersByUserId(Long userId) {
        return orderRepository.viewAllOrdersByUserId(userId);
    }
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


    @Transactional
    public Order addOrder(Long userId, String address, String paymentMethod) throws Exception {
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
        for (OrderItem orderItem : orderItems) {
            orderItemRepository.save(orderItem);
        }

        // Update stock for all products
        for (Product product : productsToUpdateStock) {
            productRepository.save(product);
        }

        order.setOrderItems(orderItems);
        order.setTotal_price(cart.getTotal_price());
        orderRepository.saveAndFlush(order);

        try {
            //delete cart after order
            cartService.removeAllProduct(user.getId());
        } catch (Exception ex) {
            Logger.getLogger(OrderService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return order;
    }

}

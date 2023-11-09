package com.anhnnd.fashionweb.service;

import com.anhnnd.fashionweb.model.Cart;
import com.anhnnd.fashionweb.model.CartItem;
import com.anhnnd.fashionweb.model.Product;
import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.repository.CartItemRepository;
import com.anhnnd.fashionweb.repository.CartRepository;
import com.anhnnd.fashionweb.repository.ProductRepository;
import com.anhnnd.fashionweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {


    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart viewCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
    @Transactional
    public Cart addProductToCart(Long customerId, Long productId, int quantity)
            throws Exception{
        Cart cart;
        cart = cartRepository.findByUserId(customerId);
        Optional<Product> itemOpt = productRepository.findById(productId);
        if (itemOpt.isEmpty()) {
            throw new Exception("Product not found!");
        }
        Product item = itemOpt.get();
        CartItem cartItem = new CartItem();
        cartItem.setProduct(item);
        cartItem.setQuantity(quantity);

        Optional<User> userOptional = userRepository.findById(customerId);
        User user = userOptional.get();
        // Get the cart for the user, or create a new cart if one doesn't exist
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }
        List<CartItem> cartItems = cart.getCartItems();
        boolean flag = true;
        for (CartItem value : cartItems) {
            if (value.getProduct().getId().equals(productId)) {
                value.setQuantity(value.getQuantity() + quantity);
                flag = false;
                break;
            }
        }
        if (flag) {
            CartItem cit = new CartItem();
            cit.setProduct(item);
            cit.setQuantity(quantity);

            cartItemRepository.save(cit);
            cartItems.add(cit);
        }
        if (cart.getProduct_quantity() == null || cart.getProduct_quantity() == 0) {
            cart.setProduct_quantity(quantity);
        } else {
            cart.setProduct_quantity(cart.getProduct_quantity() + quantity);
        }
        cart.setCartItems(cartItems);
        float total = 0;

        for (CartItem c : cartItems) {
            total += c.getProduct().getPrice() * c.getQuantity();
        }
        cart.setTotal_price(total);
        cartRepository.save(cart);
        return cart;

    }

    @Transactional
    public Cart removeProductFromCart(Long customerId, Long productId)
            throws Exception{
        Optional<User> opt = userRepository.findById(customerId);
        if (opt.isEmpty()) {
            throw new Exception("Customer not found!");
        }

        Optional<Product> itemOpt = productRepository.findById(productId);
        if (itemOpt.isEmpty()) {
            throw new Exception("Product not found!");
        }
        Product item = itemOpt.get();
        User customer = opt.get();
        Cart cart = cartRepository.findByUserId(customer.getId());
        List<CartItem> itemList = cart.getCartItems();
        boolean flag = false;
        for (int i = 0; i < itemList.size(); i++) {
            CartItem element = itemList.get(i);
            if (item.getId().equals(element.getProduct().getId())) {
                cart.setProduct_quantity(cart.getProduct_quantity() - element.getQuantity());
                itemList.remove(element);
                cartItemRepository.delete(element);
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new Exception("Product not removed from cart");
        }

        cart.setCartItems(itemList);
        float total = 0;
        for (CartItem c : itemList) {
            total += c.getProduct().getPrice() * c.getQuantity();
        }
        cart.setTotal_price(total);
        cartRepository.save(cart);
        return cart;

    }

    @Transactional
    public Cart removeAllProduct(Long customerId) throws Exception {

        Cart cart;
        cart = cartRepository.findByUserId(customerId);
        if (cart == null) {
            throw new Exception("cart not found");
        }
        cart.getCartItems().clear();
        cart.setCartItems(cart.getCartItems());
        cartItemRepository.deleteAll();
        cart.setProduct_quantity(0);
        float total = 0;
        for (CartItem ca : cart.getCartItems()) {
            total += ca.getProduct().getPrice() * ca.getQuantity();
        }
        cart.setTotal_price(total);
        return cartRepository.save(cart);

    }

    @Transactional
    public Cart increaseProductQuantity(Long customerId, Long productId)
            throws Exception {


        Optional<Product> itemOpt = productRepository.findById(productId);
        if (itemOpt.isEmpty()) {
            throw new Exception("Product not found!");
        }

        Cart cart;
        cart = cartRepository.findByUserId(customerId);
        List<CartItem> itemList = cart.getCartItems();
        boolean flag = true;
        for (int i = 0; i < itemList.size(); i++) {
            CartItem element = itemList.get(i);
            if (element.getProduct().getId().equals(productId)) {
//				cart.setProduct_quantity(cart.getProduct_quantity() + 1);
                cart.getCartItems().get(i).setQuantity(cart.getCartItems().get(i).getQuantity() + 1);
                flag = false;
            }
        }
        if (flag) {
            throw new Exception("Product not found in cart");
        }
        float total = 0;
        int totalQuantity = 0;
        for (CartItem c : itemList) {
            total += c.getProduct().getPrice() * c.getQuantity();
            totalQuantity += c.getQuantity();
        }
        cart.setTotal_price(total);
        cart.setProduct_quantity(totalQuantity);
        cartRepository.save(cart);
        return cart;
    }

    @Transactional
    public Cart decreaseProductQuantity(Long customerId, Long productId)
            throws Exception {
        Optional<User> opt = userRepository.findById(customerId);
        if (opt.isEmpty()) {
            throw new Exception("Customer not found!");
        }

        Optional<Product> itemOpt = productRepository.findById(productId);
        if (itemOpt.isEmpty()) {
            throw new Exception("Product not found!");
        }
        Product item = itemOpt.get();
        Cart cart;
        cart = cartRepository.findByUserId(customerId);
        List<CartItem> itemList = cart.getCartItems();
        boolean flag = true;
        if (!itemList.isEmpty()) {
            for (int i = 0; i < itemList.size(); i++) {
                CartItem element = itemList.get(i);
                if (element.getProduct().getId().equals(item.getId())) {
//					cart.setProduct_quantity(cart.getProduct_quantity() - 1);
                    cart.getCartItems().get(i).setQuantity(element.getQuantity() - 1);
                    if (cart.getCartItems().get(i).getQuantity() == 0) {
                        itemList.remove(element);
                    }
                    flag = false;
                }
            }
        }

        if (flag) {
            throw new Exception("Product not found in cart");
        }
        float total = 0;
        int totalQuantity = 0;
        for (CartItem c : itemList) {
            total += c.getProduct().getPrice() * c.getQuantity();
            totalQuantity += c.getQuantity();
        }
        cart.setTotal_price(total);
        cart.setProduct_quantity(totalQuantity);
        cartRepository.save(cart);
        return cart;
    }

    public Cart findByUserId(Long customerId) {
        return cartRepository.findByUserId(customerId);
    }
}

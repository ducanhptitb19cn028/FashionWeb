package com.anhnnd.fashionweb.service;

import com.anhnnd.fashionweb.model.Product;
import com.anhnnd.fashionweb.model.ProductHistory;
import com.anhnnd.fashionweb.repository.ProductHistoryRepository;
import com.anhnnd.fashionweb.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductHistoryRepository productHistoryRepository;


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        Optional<Product> optional = productRepository.findById(id);
        Product product;
        if (optional.isPresent()) {
            product = optional.get();
        } else {
            throw new RuntimeException("Product not found for id: " + id);
        }
        return product;
    }

    @Transactional
    public void addProduct(Product product) {
        productRepository.save(product);
        productHistoryRepository.save(new ProductHistory(product, product.getQuantity()));
    }


    @Transactional
    public void updateProduct(Product product) {
        productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        if (product != null) {
            // Remove associated ProductHistory entries
            List<ProductHistory> productHistories = product.getProductHistory();
            if (productHistories != null && !productHistories.isEmpty()) {
                productHistories.forEach(productHistory -> {
                    productHistory.setProduct(null);
                    productHistoryRepository.delete(productHistory);
                });
            }

            // Delete the Product entity
            productRepository.delete(product);
        }
    }
    @Transactional
    public void saveProductHistory(Product product, int newQuantity) {
        ProductHistory productHistory = new ProductHistory(product, newQuantity);
        productHistoryRepository.save(productHistory);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameStartsWith(name);
    }

    public Product getProductByNameandSize(String name, String size) {
        return productRepository.findByNameAndSize(name, size);
    }
    public List<Product> getProductByCategoryId(Long id){
        return productRepository.findAllByCategoryId(id);
    }
    public List<Product> getProductByCategoryIdAndName(Long categoryId, String productName) {
        return productRepository.findByCategoryIdAndNameContaining(categoryId, productName);
    }
}

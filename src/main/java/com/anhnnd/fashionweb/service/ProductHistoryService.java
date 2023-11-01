package com.anhnnd.fashionweb.service;

import com.anhnnd.fashionweb.model.ProductHistory;
import com.anhnnd.fashionweb.repository.ProductHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductHistoryService {
    @Autowired
    private final ProductHistoryRepository productHistoryRepository;

    public List<ProductHistory> getProductHistory(Long productId) {
        List<ProductHistory> productHistory = productHistoryRepository.findByProduct(productId);

        if (productHistory != null) {
            return productHistory;
        } else {
            return Collections.emptyList();
        }
    }
    public List<ProductHistory> getAllProductHistory() {
        return productHistoryRepository.findAll();
    }
}
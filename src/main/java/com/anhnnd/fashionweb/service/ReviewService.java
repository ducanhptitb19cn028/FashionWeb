package com.anhnnd.fashionweb.service;

import com.anhnnd.fashionweb.model.Product;
import com.anhnnd.fashionweb.model.Review;
import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public void addReview(Review review) {
        reviewRepository.save(review);
    }
}

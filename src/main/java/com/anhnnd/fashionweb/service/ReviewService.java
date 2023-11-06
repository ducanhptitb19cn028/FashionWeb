package com.anhnnd.fashionweb.service;

import com.anhnnd.fashionweb.model.Product;
import com.anhnnd.fashionweb.model.Review;
import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public void addReview(Product product, User user, String comment, int rating) {
        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setComment(comment);
        review.setRating(rating);
        reviewRepository.save(review);
    }
}

package com.anhnnd.fashionweb.repository;

import com.anhnnd.fashionweb.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
    List<Product> findAllByCategoryId(@Param("id") Long id);

    List<Product> findByNameStartsWith(String name);

    Product findByName(String name);
    Product findByNameAndSize(String name, String size);

    List<Product> findByCategoryIdAndNameContaining(Long categoryId, String productName);
}

package com.anhnnd.fashionweb.repository;

import com.anhnnd.fashionweb.model.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {
    @Query("SELECT p FROM ProductHistory p WHERE p.product.id = :pid")
    List<ProductHistory> findByProduct(@Param("pid")Long pid);
}

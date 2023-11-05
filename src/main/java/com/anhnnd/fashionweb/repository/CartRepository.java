package com.anhnnd.fashionweb.repository;

import com.anhnnd.fashionweb.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("Select c from Cart c where c.user.id = :customerId")
    Cart findByUserId(@Param("customerId")Long customerId);

}
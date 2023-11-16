package com.anhnnd.fashionweb.repository;

import com.anhnnd.fashionweb.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("SELECT o FROM Order o where o.user.id =:uid order by o.id desc" )
    public List<Order> viewAllOrdersByUserId(@Param("uid")Long uid);

    List<Order> findByOrderStatus(String order_status);
    List<Order> findByOrderStatusAndPaymentStatus(String order_status, String payment_status);

    @Query("SELECT o FROM Order o where o.orderStatus =:order_status or o.orderStatus =:order_status1 and o.paymentStatus =:payment_status order by o.id desc" )
    List<Order> findByMultipleOrderStatusAndPaymentStatus(String order_status, String order_status1, String payment_status);

    List<Order> findByPaymentStatus(String payment_status);
}

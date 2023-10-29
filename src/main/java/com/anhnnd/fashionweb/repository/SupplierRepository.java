package com.anhnnd.fashionweb.repository;


import com.anhnnd.fashionweb.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @Query("SELECT c FROM Supplier c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Supplier> findByNameContainingIgnoreCase(@Param("name")String name);

    @Query("SELECT c FROM Supplier c WHERE LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    List<Supplier> findByAddressContainingIgnoreCase(@Param("address")String address);

}

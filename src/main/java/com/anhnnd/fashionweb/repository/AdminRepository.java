package com.anhnnd.fashionweb.repository;

import com.anhnnd.fashionweb.model.Admin;
import com.anhnnd.fashionweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findById(Long adminId);
    @Query("SELECT a from Admin a where a.username =:username")
    Admin findByUsername(@Param("username")String username);

    @Query("SELECT a from Admin a where a.email =:email")
    User findByEmail(@Param("email")String email);

}
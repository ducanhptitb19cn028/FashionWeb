package com.anhnnd.fashionweb.repository;



import com.anhnnd.fashionweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findById(Long customerId);
    @Query("SELECT u from User u where u.username =:username")
    User findByUsername(@Param("username")String username);
    @Query("SELECT u from User u where u.email =:email")
    User findByEmail(@Param("email")String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
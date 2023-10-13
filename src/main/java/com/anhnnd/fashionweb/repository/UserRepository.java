package com.anhnnd.fashionweb.repository;



import com.anhnnd.fashionweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM tbl_user u where u.password = :password AND u.username = :username", nativeQuery = true)
    public User checkLogin(@Param("password") String password,
                           @Param("username") String username);
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
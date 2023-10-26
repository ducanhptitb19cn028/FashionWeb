package com.anhnnd.fashionweb.service;

import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }



    public void save(User user) {
        userRepository.save(user);
    }
}

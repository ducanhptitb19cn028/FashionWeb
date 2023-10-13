package com.anhnnd.fashionweb.controller;


import com.anhnnd.fashionweb.dto.AuthenticationRequest;
import com.anhnnd.fashionweb.dto.AuthenticationResponse;
import com.anhnnd.fashionweb.dto.RegisterRequest;
import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.repository.UserRepository;
import com.anhnnd.fashionweb.service.AuthenticationService;
import com.anhnnd.fashionweb.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AuthenticationService authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            LOGGER.error("Email {} is already taken", registerRequest.getEmail());
            return new ResponseEntity<>(new AuthenticationResponse("This email has already been taken"),HttpStatus.BAD_REQUEST);
        }
        LOGGER.info("Registering user {}", registerRequest.getEmail());
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }
    @PostMapping("/auth/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        LOGGER.info("Authenticating user {} {}", request.getEmail(), request.getPassword());
        try {
            return ResponseEntity.ok(authenticationService.authenticate(request));
        } catch (AuthenticationException e) {
            LOGGER.error("Error authenticating user {} {}", request.getEmail(), e.getMessage());
            return new ResponseEntity<>(new AuthenticationResponse("Invalid email/password supplied"), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/auth/infor")
    public ResponseEntity<Optional<User>> infor(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<User> user = userRepository.findByEmail(email);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable("user_id") Long userId) {
        User user = userRepository.findById(userId).get();
        return ResponseEntity.ok().body(user);
    }
    
}

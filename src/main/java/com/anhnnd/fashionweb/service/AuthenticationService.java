package com.anhnnd.fashionweb.service;



import com.anhnnd.fashionweb.dto.AuthenticationRequest;
import com.anhnnd.fashionweb.dto.AuthenticationResponse;
import com.anhnnd.fashionweb.dto.RegisterRequest;
import com.anhnnd.fashionweb.model.Role;
import com.anhnnd.fashionweb.model.User;
import com.anhnnd.fashionweb.repository.RoleRepository;
import com.anhnnd.fashionweb.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private RoleRepository roleRepository;
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByName("user"))));
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setYob(request.getYob());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setAvatar(request.getAvatar());
        user = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}

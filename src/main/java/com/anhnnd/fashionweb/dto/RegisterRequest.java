package com.anhnnd.fashionweb.dto;

import com.anhnnd.fashionweb.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String password;
    private String email;
    private String roles;
    private String firstname;
    private String lastname;
    private int yob;
    private String gender;
    private String phone;
    private String address;
    private String avatar;
}
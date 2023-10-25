package com.anhnnd.fashionweb.model;


import lombok.*;


import jakarta.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tbl_user")
@ToString
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id")
    private Long id;
    @Column(unique = true, name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name ="firstname")
    private String firstname;

    private String lastname;
    private int yob;
    private String gender;
    private String phone;
    private String address;
    private String avatar;
    @Transient
    private String confirmPassword;
}

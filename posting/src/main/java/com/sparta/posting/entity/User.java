package com.sparta.posting.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

//    @Pattern(regexp = "^[A-Za-z0-9]{8,15}$")
    @Column(name = "password",nullable = false)
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserRoleEnum getRole() {
        return UserRoleEnum.USER;
    }
}

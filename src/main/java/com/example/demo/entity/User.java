package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(length = 50)
    private String username;   // ✅ PK đúng DB

    @Column(length = 500, nullable = false)
    private String password;

    @Column(length = 255)
    private String fullname;

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 500)
    private String address;

    private Boolean enabled;

    private LocalDateTime lastPasswordResetDate;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<UserRole> userRoles = new ArrayList<>();;

    // ✅ OAuth2
    private String provider;
    private String providerId;
}



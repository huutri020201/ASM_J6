package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table (name = "UserRoles")
@Data
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "username")
    @JsonBackReference     // ✅ thay JsonIgnore
    User user;

    @ManyToOne
    @JoinColumn(name = "roleId")
    Role role;             // ✅ BỎ JsonIgnore
}

package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @Column(length = 20)
    private String id;   // VD: ROLE_USER, ROLE_ADMIN

    @Column(nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private List<UserRole> userRoles;
}

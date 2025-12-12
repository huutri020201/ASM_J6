package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.ProductRepo;
import com.example.demo.repository.RoleRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.repository.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
public class UserController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    UserRoleRepo userRoleRepo;
    @Autowired
    RoleRepo roleRepo;
    @GetMapping("/admin/role")
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }
    @GetMapping("admin/user")
    public List<User> findAll() {
        return userRepo.findAll();
    }
    @GetMapping("admin/user/{id}")
    public User findById(@PathVariable("id") String id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));
    }
    @PostMapping("admin/user")
    public User create(@RequestBody Map<String, Object> data) {
        String username = (String) data.get("username");
        String roleId = (String) data.get("roleId");

        User user = new User();
        user.setUsername(username);
        user.setFullname((String) data.get("fullname"));
        user.setPassword("123");
        user.setEmail((String) data.get("email"));
        user.setAddress((String) data.get("address"));
        user.setEnabled((Boolean) data.get("enabled"));

        userRepo.save(user);

        // GÁN ROLE
        Role role = roleRepo.findById(roleId).orElseThrow();

        UserRole ur = new UserRole();
        ur.setUser(user);
        ur.setRole(role);

        userRoleRepo.save(ur);

        return user;
    }

    @PutMapping("admin/user/{id}")
    public User update(@PathVariable("id") String id, @RequestBody User user) {
        return userRepo.save(user);
    }
    @DeleteMapping("admin/user/{id}")
    public void delete(@PathVariable("id") String id) {
        userRepo.deleteById(id);
    }
}

package com.example.demo.controller;

import com.example.demo.entity.Cart;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.CartRepo;
import com.example.demo.repository.RoleRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.repository.UserRoleRepo;
import com.example.demo.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("*")
@RestController
public class LoginController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;

    @PostMapping("/login")
    public Object login(@RequestBody Map<String, String> userInfo) {
        String username = userInfo.get("username");
        String password = userInfo.get("password");
        var authInfo = new UsernamePasswordAuthenticationToken(username, password);
        var authentication = authenticationManager.authenticate(authInfo);
        if (authentication.isAuthenticated()) {
            // Lấy entity User từ DB
            User userEntity = userRepo.findById(username)
                    .orElseThrow(() -> new BadCredentialsException("User không tồn tại"));

            String token = jwtService.create(userEntity, 60 * 60);
            return Map.of("token", token);
        }
        throw new BadCredentialsException("Invalid username or password");
    }

    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    UserRoleRepo userRoleRepo;
    @Autowired
    CartRepo cartRepo;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String fullname = body.get("fullname");
        String email = body.get("email");

        if (userRepo.existsById(username)) {
            throw new RuntimeException("Tài khoản đã tồn tại");
        }

        // Tạo user
        User user = new User();
        user.setUsername(username);
        user.setPassword("{noop}" + password); // hoặc passwordEncoder.encode(password)
        user.setFullname(fullname);
        user.setEmail(email);
        user.setEnabled(true);
        user.setProvider("local");

        // Lưu user trước để có reference
        userRepo.save(user);

        // --- Gán role mặc định ---
        Role defaultRole = roleRepo.findById("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role mặc định không tồn tại"));

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(defaultRole);

        userRoleRepo.save(userRole);  // Lưu vào bảng UserRoles
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepo.save(cart);

        return Map.of("message", "Đăng ký thành công");
    }

    @PostMapping("/user/change-password")
    public String changePassword(
            @RequestBody Map<String, String> data,
            Authentication authentication) {

        String username = authentication.getName(); // ✅ lấy từ token
        String oldPassword = data.get("oldPassword");
        String newPassword = data.get("newPassword");

        User user = userRepo.findById(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if (oldPassword.equalsIgnoreCase(user.getPassword())){
            throw new RuntimeException("Mật khẩu cũ không đúng");
        }

        user.setPassword("{noop}"+newPassword);
        userRepo.save(user);

        return "Đổi mật khẩu thành công";
    }

    @PutMapping("/user/profile")
    public User updateProfile(
            @RequestBody User newData,
            Authentication authentication) {

        String username = authentication.getName(); // ✅ từ JWT

        User user = userRepo.findById(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        user.setFullname(newData.getFullname());
        user.setEmail(newData.getEmail());
        user.setAddress(newData.getAddress());

        return userRepo.save(user);
    }
    @GetMapping("/user/me")
    public User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepo.findById(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
    }

}

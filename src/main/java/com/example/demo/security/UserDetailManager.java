package com.example.demo.security;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailManager implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String password = user.getPassword();
        String[] roles = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getId().substring(5))
                .toList().toArray(String[]::new);
        return org.springframework.security.core.userdetails.User.withUsername(username).password(password).roles(roles).build();
    }
}


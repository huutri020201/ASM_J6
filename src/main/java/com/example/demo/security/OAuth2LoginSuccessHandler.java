package com.example.demo.security;

import com.example.demo.entity.Cart;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserRole;
import com.example.demo.repository.CartRepo;
import com.example.demo.repository.RoleRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.repository.UserRoleRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final UserRoleRepo userRoleRepo;
    private final CartRepo cartRepo;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name  = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");

        String username = email; // dùng email làm username

        User user = userRepo.findById(username).orElse(null);

        // Nếu user chưa tồn tại → TẠO MỚI
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setFullname(name);
            user.setEnabled(true);
            user.setProvider("google");
            user.setProviderId(googleId);
            user.setPassword("{noop}oauth2");

            userRepo.save(user);

            // ✅ GÁN ROLE USER ĐÚNG CÁCH
            Role role = roleRepo.findById("ROLE_USER").orElseThrow();

            UserRole ur = new UserRole();
            ur.setUser(user);
            ur.setRole(role);
            userRoleRepo.save(ur);

            // ✅ GÁN NGƯỢC VỀ USER (BẮT BUỘC)
            user.getUserRoles().add(ur);

            // ✅ TẠO CART
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepo.save(cart);
        }


        // ✅ TẠO JWT
        String token = jwtService.create(user, 3600);

        // ✅ REDIRECT VỀ VUE
        String redirectUrl = "http://localhost:5173/oauth2/redirect?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}


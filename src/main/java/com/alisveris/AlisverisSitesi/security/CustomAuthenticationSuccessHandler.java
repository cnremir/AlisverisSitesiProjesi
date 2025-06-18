package com.alisveris.AlisverisSitesi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();


        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {

                response.sendRedirect("/adminPanel");
                return;
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                // Eğer kullanıcı ise profilePage'e yönlendir
                response.sendRedirect("/profilePage");
                return;
            }
        }

        // Eğer rol yoksa varsayılan olarak login sayfasına yönlendir
        response.sendRedirect("/login");
    }
}

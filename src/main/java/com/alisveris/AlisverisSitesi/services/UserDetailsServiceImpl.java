package com.alisveris.AlisverisSitesi.services;


import com.alisveris.AlisverisSitesi.models.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {//Security için bu sınıfı açıyoruz.

    private final UserServices userServices;

    public UserDetailsServiceImpl(UserServices userService) {
        this.userServices = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       User user = userServices.getUser(username).orElseThrow(()-> new EntityNotFoundException("Kullanıcı bulunamadı"));
        System.out.println(user.getPassword());
        System.out.println(user.getAuthorities());
        return user;
    }
}

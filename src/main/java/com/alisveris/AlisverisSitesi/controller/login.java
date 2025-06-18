package com.alisveris.AlisverisSitesi.controller;


import com.alisveris.AlisverisSitesi.dto.CreateUserRequest;
import com.alisveris.AlisverisSitesi.models.Role;
import com.alisveris.AlisverisSitesi.models.User;
import com.alisveris.AlisverisSitesi.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
public class login {

    private final UserServices userServices;

    public login(UserServices userServices) {

        this.userServices = userServices;
    }

    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, BindingResult bindingResult , Model model){
        if(bindingResult.hasErrors()){
            return "login";

        }


        model.addAttribute("message", "Registration successful!");

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name(user.getName())
                .username(user.getUsername())
                .password(user.getPassword())
                .surname(user.getSurname())
                .balance(10000.0)
                .userMailAdress(user.getUserMailAdress())
                .authorities(Set.of(Role.ROLE_USER))
                .build();

        userServices.createUser(createUserRequest);
        return "redirect:/login";
    }





}
